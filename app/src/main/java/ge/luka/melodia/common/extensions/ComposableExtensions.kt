package ge.luka.melodia.common.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

@Composable
fun PaddingValues.copy(
    start: Dp = calculateStartPadding(LocalLayoutDirection.current),
    top: Dp = calculateTopPadding(),
    end: Dp = calculateEndPadding(LocalLayoutDirection.current),
    bottom: Dp = calculateBottomPadding(),
) = PaddingValues(
    start = start,
    top = top,
    end = end,
    bottom = calculateBottomPadding() + bottom
)