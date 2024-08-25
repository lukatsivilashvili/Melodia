package ge.luka.melodia.data

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.annotation.RequiresApi
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MediaStoreLoader {

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun getSongsList(context: Context): List<SongModel> {
        val songs = mutableListOf<SongModel>()
        withContext(Dispatchers.IO) {
            val collection = Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
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

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun getAlbumList(context: Context): List<AlbumModel> {
        val albumSet = mutableSetOf<AlbumModel>()
        withContext(Dispatchers.IO) {
            val collection = MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"
            val projection = arrayOf(
                Media.ALBUM_ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
            )
            val query = context.contentResolver.query(
                /* uri = */ collection,
                /* projection = */ projection,
                /* selection = */ null,
                /* selectionArgs = */ null,
                /* sortOrder = */ sortOrder,
                /* cancellationSignal = */ null
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
                    albumSet.add(
                        AlbumModel.fromCursor(
                            cursor = cursor,
                            albumIdColumn = albumIdColumn,
                            titleColumn = titleColumn,
                            artistColumn = artistColumn,
                            songCountColumn = songCountColumn,
                        )
                    )
                }
            }
        }
        return albumSet.toList()
    }

}