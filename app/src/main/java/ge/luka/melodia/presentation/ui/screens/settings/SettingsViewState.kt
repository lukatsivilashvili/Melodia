package ge.luka.melodia.presentation.ui.screens.settings

import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme

data class SettingsViewState(
    val isDarkMode: Boolean = false,
    val currentTheme: AppTheme = AppTheme.GREEN
)