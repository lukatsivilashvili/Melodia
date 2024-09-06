package ge.luka.melodia.data

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import ge.luka.melodia.common.extensions.formatAlbumDuration
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MediaStoreLoader {

    suspend fun getSongsList(context: Context): List<SongModel> {
        val songs = mutableListOf<SongModel>()
        withContext(Dispatchers.IO) {
            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                Media.EXTERNAL_CONTENT_URI
            }
            val selection = Media.IS_MUSIC + " !=0"
            val sortOrder = "${Media.TITLE} ASC"
            val projection = arrayOf(
                MediaStore.Audio.AudioColumns._ID,
                Media.TITLE,
                Media.ALBUM,
                Media.ARTIST,
                Media.DURATION,
                Media.ALBUM_ID,
                MediaStore.Audio.AudioColumns.BITRATE,
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
        val albumSet = mutableSetOf<AlbumModel>()
        withContext(Dispatchers.IO) {
            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
            }
            val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"
            val projection = arrayOf(
                Media.ALBUM_ID,
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
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ALBUM)
                val artistColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ARTIST)
                val songCountColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS)

                while (cursor.moveToNext()) {
                    val albumId = cursor.getLong(albumIdColumn)
                    val albumDuration = getAlbumDuration(context, albumId)
                    albumSet.add(
                        AlbumModel.fromCursor(
                            cursor = cursor,
                            albumIdColumn = albumIdColumn,
                            titleColumn = titleColumn,
                            artistColumn = artistColumn,
                            songCountColumn = songCountColumn,
                            duration = albumDuration.formatAlbumDuration()
                        )
                    )
                }
            }
        }
        return albumSet.toList()
    }

    fun getAlbumDuration(context: Context, albumId: Long): Long {
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