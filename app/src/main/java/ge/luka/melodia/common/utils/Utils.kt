package ge.luka.melodia.common.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Utils {
    @Composable
    fun calculateBottomBarHeight(): Dp {
        val insets = WindowInsets.systemBars.asPaddingValues()
        val statusBarHeight = insets.calculateTopPadding()

        return BOTTOM_PLAYER_HEIGHT + statusBarHeight
    }

    val BOTTOM_PLAYER_HEIGHT = 80.dp

}