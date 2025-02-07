package ge.luka.melodia.presentation.ui.theme.themecomponents

import kotlinx.serialization.Serializable

@Serializable
enum class AppTheme {
    GREEN,
    BLUE
}

const val CURRENT_THEME = "current_theme"