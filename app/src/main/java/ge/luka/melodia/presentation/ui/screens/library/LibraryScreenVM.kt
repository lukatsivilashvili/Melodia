package ge.luka.melodia.presentation.ui.screens.library

import BaseMviViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryScreenVM @Inject constructor() :
    BaseMviViewmodel<LibraryViewState, LibraryAction, LibrarySideEffect>(initialUiState = LibraryViewState(0, "")) {

    override fun onAction(uiAction: LibraryAction) {
        when (uiAction) {
            is LibraryAction.LibraryTabClicked -> {
//                emitDestinationScreen(screen = uiAction)
            }
        }
    }

//    private fun emitDestinationScreen(screen: LibraryAction.LibraryTabClicked) {
//        when (screen.tabItem) {
//            TabScreen.Songs -> updateUiState{copy(selectedTabIndex = 0, currentTabScreen = TabScreens.Songs)}
//            TabScreens.Artists -> updateUiState{copy(selectedTabIndex = 1, currentTabScreen = TabScreens.Artists)}
//            TabScreens.Albums -> updateUiState{copy(selectedTabIndex = 2, currentTabScreen = TabScreens.Albums)}
//            TabScreens.Playlists -> updateUiState{copy(selectedTabIndex = 3, currentTabScreen = TabScreens.Playlists)}
//        }
//    }

}