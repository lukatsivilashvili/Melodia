package ge.luka.melodia.presentation.ui.screens.library

import ge.luka.melodia.presentation.ui.screens.MelodiaScreen

sealed interface LibrarySideEffect {
    data class NavigateToLibrary(val screen: MelodiaScreen = MelodiaScreen.Library) : LibrarySideEffect
    data class NavigateToSongs(val screen: MelodiaScreen = MelodiaScreen.Songs) : LibrarySideEffect
    data class NavigateToAlbums(val screen: MelodiaScreen = MelodiaScreen.Albums()) : LibrarySideEffect
    data class NavigateToArtists(val screen: MelodiaScreen = MelodiaScreen.Artists) : LibrarySideEffect
    data class NavigateToPlaylists(val screen: MelodiaScreen = MelodiaScreen.Playlists) : LibrarySideEffect
}