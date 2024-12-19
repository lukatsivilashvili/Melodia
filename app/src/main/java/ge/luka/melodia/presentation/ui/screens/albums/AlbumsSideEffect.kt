package ge.luka.melodia.presentation.ui.screens.albums

import ge.luka.melodia.domain.model.AlbumModel

sealed interface AlbumsSideEffect {
    data class ThrowToast(val message: String) : AlbumsSideEffect
    data class AlbumItemPressed(val title: String?, val albumId: Long?, val albumModel: AlbumModel?) : AlbumsSideEffect
    data class UpdateCurrentAlbum(
        val id: Long,
        val title: String,
        val artist: String,
        val artworkUri: String?
    ) : AlbumsSideEffect
}