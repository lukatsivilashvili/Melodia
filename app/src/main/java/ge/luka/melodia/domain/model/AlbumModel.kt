package ge.luka.melodia.domain.model

import android.database.Cursor
import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class AlbumModel(
    val albumId: Long? = null,
    val artistId: Long? = null,
    val title: String? = null,
    val artist: String? = null,
    val songCount: Int? = null,
    val artUri: String? = null,
    val duration: String? = null
) {
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
