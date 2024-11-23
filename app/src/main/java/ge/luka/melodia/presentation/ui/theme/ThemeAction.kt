package ge.luka.melodia.presentation.ui.theme

import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme

sealed interface ThemeAction {
    data class DarkModeReceived(val isDarkMode: Boolean) : ThemeAction
    data class ThemeColorReceived(val newTheme: AppTheme) : ThemeAction
}