package ge.luka.melodia.common.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import ge.luka.melodia.presentation.ui.components.bottomplayer.BarState

class OffsetCalculator @OptIn(ExperimentalFoundationApi::class) constructor(
    private val nowPlayingAnchors: AnchoredDraggableState<BarState>,
    private val scrollProvider: () -> Float,
    private val density: Density,
    private val isPinnedMode: Boolean,

    ) {
    @OptIn(ExperimentalFoundationApi::class)
    fun getNowPlayingOffset(): IntOffset {

        val dragProgress = scrollProvider()
        val dragOffset = nowPlayingAnchors.requireOffset().toInt();

        val baseOffset = dragOffset - (1 - dragProgress)
        val y = baseOffset.toInt()
        return IntOffset(0, y)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberCompactScreenUiState(
    screenHeightPx: Int,
    nowPlayingAnchors: AnchoredDraggableState<BarState>,
    scrollProvider: () -> Float,
    density: Density,
    isPinnedMode: Boolean,
): OffsetCalculator {


    return remember(
        screenHeightPx,
        scrollProvider,
        density,
        isPinnedMode,
    ) {
        OffsetCalculator(
            nowPlayingAnchors,
            scrollProvider,
            density,
            isPinnedMode
        )
    }

}