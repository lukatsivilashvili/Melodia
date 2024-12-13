package ge.luka.melodia.presentation.ui.screens.songs

import ge.luka.melodia.domain.model.SongModel

sealed interface SongsSideEffect {
    data class ThrowToast(val message: String) : SongsSideEffect
    data class UpdateCurrentSong(val song: SongModel) : SongsSideEffect
}