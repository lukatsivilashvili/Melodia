package ge.luka.melodia.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Utils {

    @Composable
    fun calculateBottomBarHeight(): Dp {
        val windowSize = rememberWindowSize()
    val bottomBarHeight = when(windowSize) {
        WindowType.Compact -> 90.dp
        WindowType.Medium -> 80.dp
        WindowType.Expanded -> 100.dp
    }
        return bottomBarHeight
    }


}