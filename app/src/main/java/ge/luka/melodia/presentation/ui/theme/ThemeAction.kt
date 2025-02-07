package ge.luka.melodia.presentation.ui.theme

import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme

sealed interface ThemeAction {
    data class ThemeColorReceived(val newTheme: AppTheme) : ThemeAction
}