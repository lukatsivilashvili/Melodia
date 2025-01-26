package ge.luka.melodia.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ge.luka.melodia.common.utils.Utils
import ge.luka.melodia.common.utils.Utils.BOTTOM_PLAYER_HEIGHT
import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.RepeatMode
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.components.bottomplayer.BarState
import ge.luka.melodia.presentation.ui.components.bottomplayer.BottomPlayer
import ge.luka.melodia.presentation.ui.components.bottomplayer.NowPlayingState
import ge.luka.melodia.presentation.ui.screens.nowplaying.NowPlayingScreen


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NowPlaying(
    modifier: Modifier = Modifier,
    songModel: SongModel,
    onExpandNowPlaying: () -> Unit,
    bottomPlayerPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottomPlayerPadding),
        contentAlignment = Alignment.BottomCenter
    ) {
        val configuration = LocalConfiguration.current

        val screenHeight = configuration.screenHeightDp.dp
        val density = LocalDensity.current
        val bottomPlayerHeightPx = with(density) { BOTTOM_PLAYER_HEIGHT.toPx() }
        val expandedOffset = with(density) { screenHeight.toPx() - bottomPlayerHeightPx }
        val screenHeightPx = with(density) { screenHeight.toPx() }

        val insets = WindowInsets.systemBars.asPaddingValues()
        val statusBarHeight = insets.calculateTopPadding()

        // Anchored draggable state
        val bottomPlayerDragState = remember {
            AnchoredDraggableState(
                initialValue = BarState.COLLAPSED,
                positionalThreshold = { distance -> distance * 0.5f },
                velocityThreshold = { bottomPlayerHeightPx * 0.5f },
                snapAnimationSpec = tween(),
                decayAnimationSpec = exponentialDecay(),
                confirmValueChange = { true }
            )
        }

        // Set up draggable anchors relative to the parent Box
        val anchors = remember {
            DraggableAnchors {
                BarState.COLLAPSED at 0f
                BarState.EXPANDED at -expandedOffset
            }
        }

        val offsetBottomPlayer = -(bottomPlayerDragState.offset)
        val alphaBottomPlayer = 1f - offsetBottomPlayer / (screenHeight.value)
        val alphaNowPlaying = 0f + offsetBottomPlayer / (screenHeight.value * 3.0f)

        SideEffect { bottomPlayerDragState.updateAnchors(anchors) }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = 0,
                            y = (bottomPlayerDragState.requireOffset() + screenHeightPx - bottomPlayerHeightPx).toInt()
                        )
                    }
                    .graphicsLayer { alpha = alphaNowPlaying.coerceIn(0.0f, 1.0f) }
            ) {
                NowPlayingScreen(
                    modifier = Modifier
                        .anchoredDraggable(
                            state = bottomPlayerDragState,
                            orientation = Orientation.Vertical),
                    songModel = songModel
                )
            }

            // BottomPlayer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Utils.calculateBottomBarHeight())
                    .align(Alignment.BottomCenter)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = bottomPlayerDragState
                                .requireOffset()
                                .toInt()
                        )
                    }
                    .graphicsLayer { alpha = alphaBottomPlayer }
            ) {
                BottomPlayer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Utils.calculateBottomBarHeight())
                        .clickable { onExpandNowPlaying.invoke() }
                        .anchoredDraggable(
                        state = bottomPlayerDragState,
                        orientation = Orientation.Vertical
                    ),

                    nowPlayingState = NowPlayingState.Playing(
                        song = songModel,
                        playbackState = PlayerState.PAUSED,
                        repeatMode = RepeatMode.NO_REPEAT,
                        isShuffleOn = false
                    ),
                    songProgressProvider = { 1f },
                    statusBarHeight = statusBarHeight,
                    enabled = true,
                    onTogglePlayback = {},
                    onNext = {},
                    onPrevious = {}
                )
            }
        }
    }
}

