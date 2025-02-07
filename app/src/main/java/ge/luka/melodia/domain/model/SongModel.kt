package ge.luka.melodia.domain.model

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

data class SongModel(
    override val songId: Long? = null,
    override val albumId: Long? = null,
    override val artistId: Long? = null,
    override val title: String? = null,
    override val artist: String? = null,
    override val album: String? = null,
    override val artUri: String? = null,
    val trackNumber: Int? = null,
    val duration: Long? = null,
    val songPath: String? = null,
    val bitrate: Int? = null
) : BaseModel {
    companion object {
        fun fromCursor(
            cursor: Cursor,
            albumIdColumn: Int,
            songIdColumn: Int,
            artistIdColumn: Int,
            titleColumn: Int,
            albumColumn: Int,
            artistColumn: Int,
            trackNumberColumn: Int,
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
                trackNumber = cursor.getInt(trackNumberColumn),
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

