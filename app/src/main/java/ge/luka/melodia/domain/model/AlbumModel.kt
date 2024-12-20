package ge.luka.melodia.domain.model

import android.database.Cursor
import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class AlbumModel(
    override val songId: Long? = null,
    override val albumId: Long? = null,
    override val artistId: Long? = null,
    override val title: String? = null,
    override val artist: String? = null,
    override val artUri: String? = null,
    override val album: String? = null,
    val songCount: Int? = null,
    val duration: String? = null,
): BaseModel {
    companion object {
        fun fromCursor(
            cursor: Cursor,
            albumIdColumn: Int,
            artistIdColumn: Int,
            titleColumn: Int,
            artistColumn: Int,
            songCountColumn: Int,
            duration: String
        ): AlbumModel {

            return AlbumModel(
                albumId = cursor.getLong(albumIdColumn),
                artistId = cursor.getLong(artistIdColumn),
                title = cursor.getString(titleColumn),
                artist = cursor.getString(artistColumn),
                songCount = cursor.getInt(songCountColumn),
                artUri = Uri.withAppendedPath(Uri.parse("content://media/external/audio/albumart"),
                    cursor.getLong(albumIdColumn).toString()
                ).toString(),
                duration = duration
            )
        }
    }
}
