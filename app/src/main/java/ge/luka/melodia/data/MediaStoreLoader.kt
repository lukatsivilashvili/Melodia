package ge.luka.melodia.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.core.net.toUri
import ge.luka.melodia.common.extensions.formatAlbumDuration
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.domain.model.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class MediaStoreLoader {

    private var selectedFolderUri: String? = null

    fun setSelectedFolderUri(uri: String?) {
        selectedFolderUri = uri
    }

    fun scanSongsList(context: Context): Flow<SongModel> = flow {
        val collection =
            Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val relPath = selectedFolderUri?.toUri()?.lastPathSegment
            ?.let { seg -> Uri.decode(seg).substringAfter(":") + "/" }
            ?: ""

        val selection = "${Media.RELATIVE_PATH} LIKE ? AND " + "${Media.IS_MUSIC} != 0"
        val selectionArgs = arrayOf("%$relPath%")
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
            selectionArgs,
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
            val dataColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)

            while (cursor.moveToNext()) {

                if (selectedFolderUri != null) {
                    val filePath = cursor.getString(dataColumn) // Get file path
                    if (filePath != null) {
                        emit(
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


    suspend fun getAlbumList(context: Context): List<AlbumModel> {
        val albumList = mutableListOf<AlbumModel>()
        withContext(Dispatchers.IO) {
            // Decode the user’s tree URI into a RELATIVE_PATH prefix:
            // e.g. lastPathSegment "primary:TestFolder/MelodiaMusic/Sade"
            //       → relPath "TestFolder/MelodiaMusic/Sade/"
            val uriString = selectedFolderUri
            val relPath = uriString
                ?.let { Uri.parse(it).lastPathSegment }
                ?.let { seg -> Uri.decode(seg).substringAfter(":") + "/" }
                ?: ""

            // 1) Collect album IDs under that path
            val songUri           = Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val songSelection     = "${Media.RELATIVE_PATH} LIKE ? AND ${Media.IS_MUSIC} != 0"
            val songSelectionArgs = arrayOf("%$relPath%")
            val albumIdsInFolder  = mutableSetOf<Long>()

            context.contentResolver.query(
                songUri,
                arrayOf(Media.ALBUM_ID),
                songSelection,
                songSelectionArgs,
                null
            )?.use { c ->
                val col = c.getColumnIndexOrThrow(Media.ALBUM_ID)
                while (c.moveToNext()) {
                    albumIdsInFolder.add(c.getLong(col))
                }
            }

            // 2) Now run your Albums query exactly as before, but only for those IDs
            val collection   = MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val sortOrder    = "${MediaStore.Audio.Albums.ALBUM} ASC"
            val projection   = arrayOf(
                Media.ALBUM_ID,
                MediaStore.Audio.Albums.ARTIST_ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
            )
            val albumSelection = albumIdsInFolder
                .takeIf { it.isNotEmpty() }
                ?.joinToString(prefix = "${Media.ALBUM_ID} IN (", postfix = ")")

            context.contentResolver.query(
                collection,
                projection,
                albumSelection,
                null,
                sortOrder,
                null
            )?.use { cursor ->
                val albumIdCol   = cursor.getColumnIndexOrThrow(Media.ALBUM_ID)
                val artistIdCol  = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST_ID)
                val titleCol     = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
                val artistCol    = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)
                val countCol     = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS)

                while (cursor.moveToNext()) {
                    val albumId  = cursor.getLong(albumIdCol)
                    val duration = getAlbumDuration(context, albumId).formatAlbumDuration()

                    albumList.add(
                        AlbumModel.fromCursor(
                            cursor           = cursor,
                            albumIdColumn    = albumIdCol,
                            artistIdColumn   = artistIdCol,
                            titleColumn      = titleCol,
                            artistColumn     = artistCol,
                            songCountColumn  = countCol,
                            duration         = duration
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
            val songsUri       = Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val songSelection  = "${Media.RELATIVE_PATH} LIKE ? AND ${Media.IS_MUSIC} != 0"

            // Same decoding logic for RELATIVE_PATH prefix
            val uriString = selectedFolderUri
            val relPath = uriString?.toUri()?.lastPathSegment
                ?.let { seg -> Uri.decode(seg).substringAfter(":") + "/" }
                ?: ""
            val songSelectionArgs = arrayOf("%$relPath%")

            // First pass: collect album IDs under the chosen folder
            val albumIds = mutableSetOf<Long>()
            context.contentResolver.query(
                songsUri,
                arrayOf(Media.ALBUM_ID),
                songSelection,
                songSelectionArgs,
                null
            )?.use { c ->
                val col = c.getColumnIndexOrThrow(Media.ALBUM_ID)
                while (c.moveToNext()) {
                    albumIds.add(c.getLong(col))
                }
            }

            // Then query Albums for those IDs
            if (albumIds.isNotEmpty()) {
                val albumsUri = MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
                val albumSelection = "${MediaStore.Audio.Albums._ID} IN (${albumIds.joinToString()})"

                context.contentResolver.query(
                    albumsUri,
                    arrayOf(MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ARTIST_ID),
                    albumSelection,
                    null,
                    "${MediaStore.Audio.Albums.ARTIST} ASC",
                    null
                )?.use { cursor ->
                    val artistCol   = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)
                    val artistIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST_ID)
                    val seen        = mutableSetOf<Long>()

                    while (cursor.moveToNext()) {
                        val artistId   = cursor.getLong(artistIdCol)
                        val artistName = cursor.getString(artistCol)
                        if (artistId !in seen && !artistName.isNullOrEmpty() && artistName != "<unknown>") {
                            seen.add(artistId)
                            artistList.add(
                                ArtistModel(
                                    artistId = artistId,
                                    title    = artistName,
                                    artUri   = getArtistFirstAlbumArt(context, artistId)
                                )
                            )
                        }
                    }
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
                    "content://media/external/audio/albumart".toUri(),
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