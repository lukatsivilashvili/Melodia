package ge.luka.melodia.domain.model

import android.database.Cursor

data class ArtistModel(
    val id: Long? = null,
    val title: String?,
    val albumCount: Int?,
    val songCount: Int?,
    val albums: List<AlbumModel>
) {
    companion object {
        fun fromCursor(
            cursor: Cursor,
            id: Long,
            artistColumn: Int,
            artistAlbumCountColumn: Int,
            artistSongCountColumn: Int,
            albums: List<AlbumModel>
        ): ArtistModel = ArtistModel(
            id = id,
            title = cursor.getString(artistColumn),
            albumCount = cursor.getInt(artistAlbumCountColumn),
            songCount = cursor.getInt(artistSongCountColumn),
            albums = albums
        )
    }
}
