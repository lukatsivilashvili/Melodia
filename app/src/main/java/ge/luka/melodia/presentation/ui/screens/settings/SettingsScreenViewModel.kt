package ge.luka.melodia.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.repository.DataStoreRepository
import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

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

    fun setIsDarkMode(isDarkMode: Boolean) = viewModelScope.launch {
        dataStoreRepository.setDarkMode(isDarkMode)
    }

    fun setCurrentTheme(theme: AppTheme) = viewModelScope.launch {
        dataStoreRepository.setCurrentTheme(theme)
    }


}