package ge.luka.melodia.presentation.ui.screens.albums

sealed interface AlbumsSideEffect {
    data class ThrowToast(val message: String) : AlbumsSideEffect
    data class AlbumItemPressed(
        val albumId: Long,
        val albumTitle: String,
        val albumArtist: String,
        val albumArt: String,
        val albumDuration: String
    ) : AlbumsSideEffect

    data class UpdateCurrentAlbum(
        val id: Long,
        val title: String,
        val artist: String,
        val artworkUri: String?
    ) : AlbumsSideEffect
}