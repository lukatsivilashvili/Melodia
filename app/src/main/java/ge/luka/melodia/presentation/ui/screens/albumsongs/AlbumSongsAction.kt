package ge.luka.melodia.presentation.ui.screens.albumsongs

import ge.luka.melodia.domain.model.SongModel

sealed interface AlbumSongsAction {
    data object PlayPressed : AlbumSongsAction
    data object ShufflePressed : AlbumSongsAction
    data class SongPressed(val song: SongModel) : AlbumSongsAction
}