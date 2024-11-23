package ge.luka.melodia.presentation.ui.screens.library

import BaseMviViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryScreenVM @Inject constructor() :
    BaseMviViewmodel<LibraryViewState, LibraryAction, LibrarySideEffect>(initialUiState = LibraryViewState()) {

    override fun onAction(uiAction: LibraryAction) {
        when (uiAction) {
            is LibraryAction.LibraryItemClicked -> {
                emitDestinationScreen(screen = uiAction)
            }
        }
    }

    private fun emitDestinationScreen(screen: LibraryAction.LibraryItemClicked) {
        when (screen.libraryItem) {
            "Songs" -> emitSideEffect(LibrarySideEffect.NavigateToSongs())
            "Albums" -> emitSideEffect(LibrarySideEffect.NavigateToAlbums())
            "Artists" -> emitSideEffect(LibrarySideEffect.NavigateToArtists())
            "Playlists" -> emitSideEffect(LibrarySideEffect.NavigateToPlaylists())
            else -> emitSideEffect(LibrarySideEffect.NavigateToLibrary())
        }
    }

}