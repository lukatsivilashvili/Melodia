package ge.luka.melodia.presentation.ui.screens.artists

sealed interface ArtistsSideEffect {
    data class NavigateToAlbums(val artistId: Long, val artistName: String) : ArtistsSideEffect
}