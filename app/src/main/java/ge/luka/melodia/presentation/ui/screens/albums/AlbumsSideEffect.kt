package ge.luka.melodia.presentation.ui.screens.albums

sealed interface AlbumsSideEffect {
    data class ThrowToast(val message: String) : AlbumsSideEffect
}