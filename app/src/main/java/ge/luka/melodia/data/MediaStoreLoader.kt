package ge.luka.melodia.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log.d
import ge.luka.melodia.common.extensions.formatAlbumDuration
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.domain.model.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MediaStoreLoader {

    private var selectedFolderUri: Uri? = null

    fun setSelectedFolderUri(uri: Uri?) {
        selectedFolderUri = uri
    }

    suspend fun getSongsList(context: Context): List<SongModel> {
        val songs = mutableListOf<SongModel>()
        withContext(Dispatchers.IO) {
            val collection =
                Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val selection = "${Media.IS_MUSIC} != 0"
            val sortOrder = "${Media.TITLE} ASC"
            val projection = arrayOf(
                MediaStore.Audio.AudioColumns._ID,
                Media.TITLE,
                Media.ALBUM,
                Media.ARTIST,
                Media.DURATION,
                Media.TRACK,
                Media.ALBUM_ID,
                Media.ARTIST_ID,
                Media.RELATIVE_PATH,
                MediaStore.Audio.AudioColumns.BITRATE,
                MediaStore.Audio.AudioColumns.DATA
            )
            val query = context.contentResolver.query(
                collection,
                projection,
                selection,
                null,
                sortOrder,
                null
            )
            query?.use { cursor ->

                val songIdColumn = cursor.getColumnIndexOrThrow(Media._ID)
                val albumIdColumn = cursor.getColumnIndexOrThrow(Media.ALBUM_ID)
                val artistIdColumn = cursor.getColumnIndexOrThrow(Media.ARTIST_ID)
                val titleColumn = cursor.getColumnIndexOrThrow(Media.TITLE)
                val albumColumn = cursor.getColumnIndexOrThrow(Media.ALBUM)
                val artistColumn = cursor.getColumnIndexOrThrow(Media.ARTIST)
                val trackNumberColumn = cursor.getColumnIndexOrThrow(Media.TRACK)
                val durationColumn = cursor.getColumnIndexOrThrow(Media.DURATION)
                val bitrateColumn = cursor.getColumnIndexOrThrow(Media.BITRATE)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)

                while (cursor.moveToNext()) {

                    if (selectedFolderUri != null) {
                        val filePath = cursor.getString(dataColumn) // Get file path
                        if (filePath != null) {
                            songs.add(
                                SongModel.fromCursor(
                                    cursor,
                                    songIdColumn = songIdColumn,
                                    albumIdColumn = albumIdColumn,
                                    artistIdColumn = artistIdColumn,
                                    titleColumn = titleColumn,
                                    albumColumn = albumColumn,
                                    trackNumberColumn = trackNumberColumn,
                                    artistColumn = artistColumn,
                                    durationColumn = durationColumn,
                                    bitrateColumn = bitrateColumn
                                )
                            )
                        }
                    }

                }
            }
        }
        val duplicates = songs.groupingBy { it.title }
            .eachCount()
            .filter { it.value > 1 }
            .keys
            .toList()

        if (duplicates.isNotEmpty()) {
            d("dupes","Duplicate songs found:")
            songs.filter { it.title in duplicates }.forEach { println("  - ${it.title} by ${it.artist}") }
        } else {
            d("dupes","No duplicate songs found.")
        }
        return songs
    }

    suspend fun getAlbumList(context: Context): List<AlbumModel> {
        val albumList = mutableListOf<AlbumModel>()
        withContext(Dispatchers.IO) {
            val collection = MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"
            val projection = arrayOf(
                Media.ALBUM_ID,
                MediaStore.Audio.Albums.ARTIST_ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
            )

            val query = context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder,
                null
            )


            query?.use { cursor ->
                val albumIdColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ALBUM_ID)
                val artistIdColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ARTIST_ID)
                val titleColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ALBUM)
                val artistColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ARTIST)
                val songCountColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS)

                while (cursor.moveToNext()) {
                    val albumId = cursor.getLong(albumIdColumn)
                    val albumDuration = getAlbumDuration(
                        context,
                        albumId
                    )
                    albumList.add(
                        AlbumModel.fromCursor(
                            cursor = cursor,
                            albumIdColumn = albumIdColumn,
                            artistIdColumn = artistIdColumn,
                            titleColumn = titleColumn,
                            artistColumn = artistColumn,
                            songCountColumn = songCountColumn,
                            duration = albumDuration.formatAlbumDuration()
                        )
                    )
                }
            }
        }
        return albumList
    }

    suspend fun getArtistsList(context: Context): List<ArtistModel> {
        val artistList = mutableListOf<ArtistModel>()
        withContext(Dispatchers.IO) {
            // Query albums instead of songs to get primary artists
            val albumsUri = MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
            // First, get all songs from our directory to get their album IDs
            val songsUri = Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val songSelection = "${Media.RELATIVE_PATH} LIKE ? AND ${Media.IS_MUSIC} != 0"
            val songSelectionArgs = arrayOf("%MelodiaMusic%")

            // Get unique album IDs from our directory
            val albumIds = mutableSetOf<Long>()
            context.contentResolver.query(
                songsUri,
                arrayOf(Media.ALBUM_ID),
                songSelection,
                songSelectionArgs,
                null
            )?.use { cursor ->
                val albumIdColumn = cursor.getColumnIndexOrThrow(Media.ALBUM_ID)
                while (cursor.moveToNext()) {
                    albumIds.add(cursor.getLong(albumIdColumn))
                }
            }

            // If we found any albums, query for their artists
            if (albumIds.isNotEmpty()) {
                val albumSelection =
                    "${MediaStore.Audio.Albums._ID} IN (${albumIds.joinToString(",")})"

                val albumQuery = context.contentResolver.query(
                    albumsUri,
                    arrayOf(
                        MediaStore.Audio.Albums.ARTIST,
                        MediaStore.Audio.Albums.ARTIST_ID
                    ),
                    albumSelection,
                    null,
                    "${MediaStore.Audio.Albums.ARTIST} ASC"
                )

                val artistsMap = mutableMapOf<String, Long>()
                albumQuery?.use { cursor ->
                    val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)
                    val artistIdColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST_ID)

                    while (cursor.moveToNext()) {
                        val artistName = cursor.getString(artistColumn)
                        val artistId = cursor.getLong(artistIdColumn)
                        if (artistName != null && artistName.isNotEmpty() && artistName != "<unknown>") {
                            artistsMap[artistName] = artistId
                        }
                    }
                }

                // Create artist models for each unique artist
                artistsMap.forEach { (artistName, artistId) ->
                    artistList.add(
                        ArtistModel(
                            artistId = artistId,
                            title = artistName,
                            artUri = getArtistFirstAlbumArt(context, artistId)
                        )
                    )
                }
            }
        }
        return artistList.sortedBy { it.title }
    }

    private fun getArtistFirstAlbumArt(context: Context, artistId: Long): String? {
        val albumsUri = MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(MediaStore.Audio.Albums._ID)
        val selection = "${MediaStore.Audio.Albums.ARTIST_ID} = ?"
        val selectionArgs = arrayOf(artistId.toString())
        val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"

        val cursor = context.contentResolver.query(
            albumsUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val albumId = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID))
                return Uri.withAppendedPath(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId.toString()
                ).toString()
            }
        }
        return null
    }

    private fun getAlbumDuration(
        context: Context,
        albumId: Long
    ): Long {
        val uri = Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(Media.DURATION)
        val selection = "${Media.ALBUM_ID} = ?"
        val selectionArgs = arrayOf(albumId.toString())

        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        var totalDuration = 0L

        cursor?.use {
            while (it.moveToNext()) {
                val duration = it.getLong(it.getColumnIndexOrThrow(Media.DURATION))
                totalDuration += duration
            }
        }

        return totalDuration
    }
}