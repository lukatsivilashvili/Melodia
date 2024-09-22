package ge.luka.melodia.domain.model

import android.database.Cursor

data class ArtistModel(
    val title: String?,
    val albumCount: Int?,
    val songCount: Int?,
    val albums: Set<AlbumModel>
) {
    companion object {
        fun fromCursor(
            cursor: Cursor,
            artistColumn: Int,
            artistAlbumCountColumn: Int,
            artistSongCountColumn: Int,
            albums: Set<AlbumModel>
        ): ArtistModel = ArtistModel(
            title = cursor.getString(artistColumn),
            albumCount = cursor.getInt(artistAlbumCountColumn),
            songCount = cursor.getInt(artistSongCountColumn),
            albums = albums
        )
    }
}
