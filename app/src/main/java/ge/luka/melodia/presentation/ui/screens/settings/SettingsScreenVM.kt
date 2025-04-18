package ge.luka.melodia.presentation.ui.screens.settings

import BaseMviViewmodel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.common.extensions.launch
import ge.luka.melodia.domain.repository.ThemeRepository
import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenVM @Inject constructor(
    private val themeRepository: ThemeRepository
) : BaseMviViewmodel<SettingsViewState, SettingsAction, SettingsSideEffect>(
    initialUiState = SettingsViewState()
) {
    init {
        launch {
            themeRepository.getCurrentTheme()
                .map { currentTheme -> updateUiState { copy(currentTheme = currentTheme) } }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = AppTheme.GREEN
                )
        }
    }

    override fun onAction(uiAction: SettingsAction) {
        when (uiAction) {
            is SettingsAction.ThemeReceived -> updateUiState {
                copy(currentTheme = uiAction.receivedTheme)
            }

            is SettingsAction.ThemeChanged -> {
                updateUiState { copy(currentTheme = uiAction.newTheme) }
                setCurrentTheme(theme = uiAction.newTheme)            }
        }
    }
    private fun setCurrentTheme(theme: AppTheme) = viewModelScope.launch {
        themeRepository.setCurrentTheme(theme)
    }
}