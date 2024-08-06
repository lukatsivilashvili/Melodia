package ge.luka.melodia.presentation.ui.library

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.presentation.ui.MelodiaScreen
import javax.inject.Inject

@HiltViewModel
class LibraryScreenViewModel @Inject constructor() : ViewModel() {

    fun setDestinationScreen(screen: String): MelodiaScreen {
        return when (screen) {
            "Library" -> MelodiaScreen.Library
            "Songs" -> MelodiaScreen.Songs
            "Albums" -> MelodiaScreen.Albums
            "Artists" -> MelodiaScreen.Artists
            "Playlists" -> MelodiaScreen.Playlists
            else -> MelodiaScreen.Library
        }
    }

}