package ge.luka.melodia.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class WindowType {
    Compact, Medium, Expanded
}

@Composable
fun rememberWindowSize(): WindowType {
    val configuration = LocalConfiguration.current

    return when {
        configuration.densityDpi < 600 -> WindowType.Compact
        configuration.densityDpi < 840 -> WindowType.Medium
        else -> WindowType.Expanded
    }
}