package ge.luka.melodia.presentation.ui.screens.artists

sealed interface ArtistsAction {
    data class OnArtistClicked(val artistId: Long, val artistName: String) : ArtistsAction
}