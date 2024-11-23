package ge.luka.melodia.presentation.ui.screens.albums

import ge.luka.melodia.domain.model.AlbumModel

sealed interface AlbumsAction {
    data class AlbumPressed(val album: AlbumModel) : AlbumsAction
}