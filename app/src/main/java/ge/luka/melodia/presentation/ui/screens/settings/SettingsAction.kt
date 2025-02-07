package ge.luka.melodia.presentation.ui.screens.settings

import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme

sealed interface SettingsAction {
    data class ThemeReceived(val receivedTheme: AppTheme) : SettingsAction
    data class ThemeChanged(val newTheme: AppTheme) : SettingsAction
}