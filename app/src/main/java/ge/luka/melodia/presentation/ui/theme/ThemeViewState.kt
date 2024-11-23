package ge.luka.melodia.presentation.ui.theme

import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme

data class ThemeViewState(
    val isDarkMode: Boolean = false,
    val currentTheme: AppTheme = AppTheme.GREEN
)
