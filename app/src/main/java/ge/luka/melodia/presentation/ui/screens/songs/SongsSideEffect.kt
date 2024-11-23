package ge.luka.melodia.presentation.ui.screens.songs

sealed interface SongsSideEffect {
    data class ThrowToast(val message: String) : SongsSideEffect
}