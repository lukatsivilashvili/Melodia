package ge.luka.melodia.presentation.ui.screens.settings

import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme

sealed interface SettingsSideEffect {

    data class DarkModeSwitched(val isDarkMode: Boolean) : SettingsSideEffect
    data class ThemeChanged(val newTheme: AppTheme) : SettingsSideEffect

}