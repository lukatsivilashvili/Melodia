package ge.luka.melodia.presentation.ui.theme

import kotlinx.serialization.Serializable

@Serializable
enum class AppTheme {
    GREEN,
    BLUE
}

const val IS_DARK_MODE = "is_dark_mode"
const val CURRENT_THEME = "current_theme"