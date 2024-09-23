package ge.luka.melodia.presentation.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.repository.DataStoreRepository
import ge.luka.melodia.domain.repository.MediaStoreRepository
import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeVM @Inject constructor(
    dataStoreRepository: DataStoreRepository,
    mediaStoreRepository: MediaStoreRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            mediaStoreRepository.getAllSongs().collect { println(it) }
            mediaStoreRepository.getAllAlbums().collect { println(it) }
            mediaStoreRepository.getAllArtists().collect { println(it) }
        }
    }

    // Observe the DataStore flow for dynamic theme preference
    var isDarkMode: StateFlow<Boolean> =
        dataStoreRepository.getDarkMode().map { isDarkMode -> isDarkMode }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )
        private set

    // Observe the DataStore flow for theme type preference
    var currentTheme: StateFlow<AppTheme> =
        dataStoreRepository.getCurrentTheme().map { currentTheme -> currentTheme }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AppTheme.GREEN
        )
        private set
}