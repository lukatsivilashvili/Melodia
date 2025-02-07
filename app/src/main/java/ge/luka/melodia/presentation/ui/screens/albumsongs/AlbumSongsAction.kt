package ge.luka.melodia.presentation.ui.screens.albumsongs

import ge.luka.melodia.domain.model.SongModel

sealed interface AlbumSongsAction {
    data class PlayPressed(val songs: List<SongModel>) : AlbumSongsAction
    data class ShufflePressed(val songs: List<SongModel>) : AlbumSongsAction
    data class SongPressed(val songs: List<SongModel>, val index: Int) : AlbumSongsAction
}