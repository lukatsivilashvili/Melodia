package ge.luka.melodia.domain.model

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

data class SongModel(
    val songId: Long? = null,
    val albumId: Long? = null,
    val artistId: Long? = null,
    val title: String? = null,
    val album: String? = null,
    val artist: String? = null,
    val duration: Long? = null,
    val songPath: String? = null,
    val artUri: String? = null,
    val bitrate: Int? = null
) {
    companion object {
        fun fromCursor(
            cursor: Cursor,
            albumIdColumn: Int,
            songIdColumn: Int,
            artistIdColumn: Int,
            titleColumn: Int,
            albumColumn: Int,
            artistColumn: Int,
            durationColumn: Int,
            bitrateColumn: Int
        ): SongModel {
            return SongModel(
                songId = cursor.getLong(songIdColumn),
                albumId = cursor.getLong(albumIdColumn),
                artistId = cursor.getLong(artistIdColumn),
                title = cursor.getString(titleColumn),
                album = cursor.getString(albumColumn),
                artist = cursor.getString(artistColumn),
                duration = cursor.getLong(durationColumn),
                songPath = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(songIdColumn)
                ).toString(),
                artUri = Uri.withAppendedPath(/* baseUri = */ Uri.parse("content://media/external/audio/albumart"),
                    cursor.getLong(albumIdColumn).toString()
                ).toString(),
                bitrate = cursor.getInt(bitrateColumn)
            )
        }
    }
}

