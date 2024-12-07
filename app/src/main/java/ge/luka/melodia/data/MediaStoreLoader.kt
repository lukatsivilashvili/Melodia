package ge.luka.melodia.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import ge.luka.melodia.common.extensions.formatAlbumDuration
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.domain.model.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MediaStoreLoader {

    suspend fun getSongsList(context: Context): List<SongModel> {
        val songs = mutableListOf<SongModel>()
        withContext(Dispatchers.IO) {
            val collection =
                Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val selection = "${Media.RELATIVE_PATH} LIKE ? AND " + "${Media.IS_MUSIC} != 0"
            val selectionArgs = arrayOf("%MelodiaMusic%")
            val sortOrder = "${Media.TITLE} ASC"
            val projection = arrayOf(
                MediaStore.Audio.AudioColumns._ID,
                Media.TITLE,
                Media.ALBUM,
                Media.ARTIST,
                Media.DURATION,
                Media.ALBUM_ID,
                Media.RELATIVE_PATH,
                MediaStore.Audio.AudioColumns.BITRATE,
            )
            val query = context.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder,
                null
            )
            query?.use { cursor ->

                val songIdColumn = cursor.getColumnIndexOrThrow(Media._ID)
                val albumIdColumn = cursor.getColumnIndexOrThrow(Media.ALBUM_ID)
                val titleColumn = cursor.getColumnIndexOrThrow(Media.TITLE)
                val albumColumn = cursor.getColumnIndexOrThrow(Media.ALBUM)
                val artistColumn = cursor.getColumnIndexOrThrow(Media.ARTIST)
                val durationColumn = cursor.getColumnIndexOrThrow(Media.DURATION)
                val bitrateColumn = cursor.getColumnIndexOrThrow(Media.BITRATE)

                while (cursor.moveToNext()) {
                    songs.add(
                        SongModel.fromCursor(
                            cursor,
                            albumIdColumn = albumIdColumn,
                            titleColumn = titleColumn,
                            albumColumn = albumColumn,
                            artistColumn = artistColumn,
                            durationColumn = durationColumn,
                            songIdColumn = songIdColumn,
                            bitrateColumn = bitrateColumn
                        )
                    )
                }
            }
        }
        return songs
    }

    suspend fun getAlbumList(context: Context): List<AlbumModel> {
        val albumList = mutableListOf<AlbumModel>()
        withContext(Dispatchers.IO) {
            // First get all songs from our directory to get their album IDs
            val songsUri = Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val songSelection = "${Media.RELATIVE_PATH} LIKE ? AND ${Media.IS_MUSIC} != 0"
            val songSelectionArgs = arrayOf("%MelodiaMusic%")

            val songQuery = context.contentResolver.query(
                songsUri,
                arrayOf(Media.ALBUM_ID),
                songSelection,
                songSelectionArgs,
                null
            )

            // Create a set of album IDs from our directory
            val albumIds = mutableSetOf<Long>()
            songQuery?.use { cursor ->
                val albumIdColumn = cursor.getColumnIndexOrThrow(Media.ALBUM_ID)
                while (cursor.moveToNext()) {
                    albumIds.add(cursor.getLong(albumIdColumn))
                }
            }

            // Now query albums but only for those IDs
            if (albumIds.isNotEmpty()) {
                val collection = MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
                val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"
                val projection = arrayOf(
                    Media.ALBUM_ID,
                    MediaStore.Audio.Albums.ARTIST_ID,
                    MediaStore.Audio.Albums.ALBUM,
                    MediaStore.Audio.Albums.ARTIST,
                    MediaStore.Audio.Albums.NUMBER_OF_SONGS
                )

                val selection = "${Media.ALBUM_ID} IN (${albumIds.joinToString(",")})"

                val query = context.contentResolver.query(
                    collection,
                    projection,
                    selection,
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
        }
        return albumList
    }

    suspend fun getArtistsList(context: Context): List<ArtistModel> {
        val artistList = mutableListOf<ArtistModel>()
        withContext(Dispatchers.IO) {
            // First get all songs from our directory to get their artist IDs
            val songsUri = Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val songSelection = "${Media.RELATIVE_PATH} LIKE ? AND ${Media.IS_MUSIC} != 0"
            val songSelectionArgs = arrayOf("%MelodiaMusic%")

            val songQuery = context.contentResolver.query(
                songsUri,
                arrayOf(Media.ARTIST_ID, Media.ARTIST),
                songSelection,
                songSelectionArgs,
                null
            )

            val artistsMap = mutableMapOf<String, Long>() // Artist name to ID mapping
            songQuery?.use { cursor ->
                val artistColumn = cursor.getColumnIndexOrThrow(Media.ARTIST)
                val artistIdColumn = cursor.getColumnIndexOrThrow(Media.ARTIST_ID)

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
        return artistList.sortedBy { it.title }
    }

    private fun getAlbumDuration(
        context: Context,
        albumId: Long
    ): Long {
        val uri = Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(Media.DURATION)
        val selection = "${Media.ALBUM_ID} = ? AND ${Media.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf(albumId.toString(), "%MelodiaMusic%")

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

    private fun getArtistFirstAlbumArt(
        context: Context,
        artistId: Long
    ): String? {
        // First get album IDs for this artist from our directory
        val songsUri = Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val selection = "${Media.ARTIST_ID} = ? AND ${Media.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf(artistId.toString(), "%MelodiaMusic%")

        val query = context.contentResolver.query(
            songsUri,
            arrayOf(Media.ALBUM_ID),
            selection,
            selectionArgs,
            null
        )
        var artistArtUri: String? = null

        query?.use { cursor ->
            if (cursor.moveToFirst()) {
                val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(Media.ALBUM_ID))
                artistArtUri = Uri.withAppendedPath(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId.toString()
                ).toString()
            } else null
        }

        return artistArtUri
    }
}