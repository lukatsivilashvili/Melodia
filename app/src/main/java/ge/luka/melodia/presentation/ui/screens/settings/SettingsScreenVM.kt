package ge.luka.melodia.presentation.ui.screens.settings

import BaseMviViewmodel
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

@HiltViewModel
class SettingsScreenVM @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : BaseMviViewmodel<SettingsViewState, SettingsAction, SettingsSideEffect>(
    initialUiState = SettingsViewState()
) {
    override fun onAction(uiAction: SettingsAction) {
        when (uiAction) {
            is SettingsAction.DarkModeReceived -> updateUiState {
                copy(isDarkMode = uiAction.receivedDarkMode)
            }

            is SettingsAction.ThemeReceived -> updateUiState {
                copy(currentTheme = uiAction.receivedTheme)
            }

            is SettingsAction.DarkModeSwitched -> {
                if (uiState.value.isDarkMode) {
                    emitSideEffect(SettingsSideEffect.DarkModeSwitched(isDarkMode = false))
                } else {
                    emitSideEffect(SettingsSideEffect.DarkModeSwitched(isDarkMode = true))
                }
            }

            is SettingsAction.ThemeChanged -> {
                emitSideEffect(SettingsSideEffect.ThemeChanged(newTheme = uiAction.newTheme))
            }
        }
    }

    // Observe the DataStore flow for dynamic theme preference
    val isDarkMode: StateFlow<Boolean> =
        dataStoreRepository.getDarkMode().map { isDarkMode -> isDarkMode }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    // Observe the DataStore flow for theme type preference
    val currentTheme: StateFlow<AppTheme> =
        dataStoreRepository.getCurrentTheme().map { currentTheme -> currentTheme }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AppTheme.GREEN
        )

    fun setIsDarkMode(isDarkMode: Boolean) = viewModelScope.launch {
        dataStoreRepository.setDarkMode(isDarkMode)
    }

    fun setCurrentTheme(theme: AppTheme) = viewModelScope.launch {
        dataStoreRepository.setCurrentTheme(theme)
    }


}