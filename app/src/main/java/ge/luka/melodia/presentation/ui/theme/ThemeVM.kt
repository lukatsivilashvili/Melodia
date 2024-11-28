package ge.luka.melodia.presentation.ui.theme

import BaseMviViewmodel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.repository.DataStoreRepository
import ge.luka.melodia.domain.repository.MediaStoreRepository
import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeVM @Inject constructor(
    dataStoreRepository: DataStoreRepository,
    mediaStoreRepository: MediaStoreRepository
) : BaseMviViewmodel<ThemeViewState, ThemeAction, ThemeSideEffect>(
    initialUiState = ThemeViewState()
) {
    override fun onAction(uiAction: ThemeAction) {
        when (uiAction) {
            is ThemeAction.DarkModeReceived -> updateUiState {
                copy(isDarkMode = uiAction.isDarkMode)
            }

            is ThemeAction.ThemeColorReceived -> updateUiState {
                copy(currentTheme = uiAction.newTheme)
            }
        }
    }

    init {
        viewModelScope.launch {
            mediaStoreRepository.cacheAllSongs()
            mediaStoreRepository.cacheAllAlbums()
            mediaStoreRepository.cacheAllArtists()

            dataStoreRepository.getDarkMode().map { isDarkMode ->
                updateUiState { copy(isDarkMode = isDarkMode) }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = false
            )

            dataStoreRepository.getCurrentTheme().map { currentTheme ->
                updateUiState { copy(currentTheme = currentTheme) }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = AppTheme.GREEN
            )
        }
    }
}