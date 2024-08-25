package ge.luka.melodia.domain.model

import android.database.Cursor
import android.net.Uri

data class AlbumModel(
    val albumId: Long? = null,
    val title: String? = null,
    val artist: String? = null,
    val songCount: Int? = null,
    val artUri: String? = null,
) {
    companion object {
        fun fromCursor(
            cursor: Cursor,
            albumIdColumn: Int,
            titleColumn: Int,
            artistColumn: Int,
            songCountColumn: Int,
        ): AlbumModel {

            return AlbumModel(
                albumId = cursor.getLong(albumIdColumn),
                title = cursor.getString(titleColumn),
                artist = cursor.getString(artistColumn),
                songCount = cursor.getInt(songCountColumn),
                artUri = Uri.withAppendedPath(/* baseUri = */ Uri.parse("content://media/external/audio/albumart"),
                    cursor.getLong(albumIdColumn).toString()
                ).toString()
            )
        }
    }
}
