package ge.luka.melodia.presentation.ui.screens.songs

import ge.luka.melodia.domain.model.SongModel

sealed interface SongsAction {
    data object PlayPressed : SongsAction
    data object ShufflePressed : SongsAction
    data class SongPressed(val song: SongModel) : SongsAction
}