package ge.luka.melodia.presentation.ui.screens.albums

import ge.luka.melodia.domain.model.AlbumModel

sealed interface AlbumsAction {
    data object DialogDismiss : AlbumsAction
    data class AlbumItemPressed(
        val albumId: Long,
        val albumTitle: String,
        val albumArtist: String,
        val albumArt: String,
        val albumDuration: String
    ) : AlbumsAction

    data class AlbumLongPressed(val album: AlbumModel) : AlbumsAction
    data class MetadataSaved(
        val id: Long,
        val title: String,
        val artist: String,
        val artworkUri: String?
    ) : AlbumsAction
}