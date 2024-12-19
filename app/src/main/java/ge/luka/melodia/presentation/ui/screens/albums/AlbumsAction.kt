package ge.luka.melodia.presentation.ui.screens.albums

import ge.luka.melodia.domain.model.AlbumModel

sealed interface AlbumsAction {
    data object DialogDismiss : AlbumsAction
    data class AlbumItemPressed(val title: String?, val albumId: Long?, val albumModel: AlbumModel?) : AlbumsAction
    data class AlbumLongPressed(val album: AlbumModel) : AlbumsAction
    data class MetadataSaved(
        val id: Long,
        val title: String,
        val artist: String,
        val artworkUri: String?
    ) : AlbumsAction
}