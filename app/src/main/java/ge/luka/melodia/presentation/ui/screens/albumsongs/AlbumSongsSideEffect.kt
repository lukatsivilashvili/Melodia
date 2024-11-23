package ge.luka.melodia.presentation.ui.screens.albumsongs

sealed interface AlbumSongsSideEffect {
    data class ThrowToast(val message: String) : AlbumSongsSideEffect
}