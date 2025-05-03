package ge.luka.melodia.presentation.ui.screens.nowplaying

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ge.luka.melodia.common.utils.Utils
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.components.bottomplayer.BarState
import ge.luka.melodia.presentation.ui.components.bottomplayer.BottomPlayer
import ge.luka.melodia.presentation.ui.screens.nowplaying.components.NowPlayingScreen
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NowPlaying(
    modifier: Modifier = Modifier,
    viewModel: NowPlayingVM = hiltViewModel(),
    onShowBottomPlayer: @Composable () -> Unit,
    bottomPlayerPadding: PaddingValues
) {

    val viewState by viewModel.uiState.collectAsStateWithLifecycle()
    var invoked by remember { mutableStateOf(false) } // Track invocation

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val density = LocalDensity.current
    val bottomPlayerHeightPx = with(density) { Utils.calculateBottomBarHeight().toPx() }
    val expandedOffset = with(density) { screenHeight.toPx() - bottomPlayerHeightPx }
    val screenHeightPx = with(density) { screenHeight.toPx() }

    val insets = WindowInsets.systemBars.asPaddingValues()
    val statusBarHeight = insets.calculateTopPadding()

    if (viewState.shouldShowBottomPlayer) {
        if (!invoked) {
            onShowBottomPlayer.invoke()
            invoked = true
        }
    } else {
        invoked = false // Reset when bottom player is hidden
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottomPlayerPadding),
        contentAlignment = Alignment.BottomCenter
    ) {

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
        val alphaNowPlaying = 0f + offsetBottomPlayer / (screenHeight.value)

        val coroutineScope = rememberCoroutineScope()

        fun expandBottomPlayer() {
            coroutineScope.launch {
                bottomPlayerDragState.animateTo(BarState.EXPANDED)
            }
        }

        fun collapseBottomPlayer() {
            coroutineScope.launch {
                bottomPlayerDragState.animateTo(BarState.COLLAPSED)
            }
        }

        SideEffect { bottomPlayerDragState.updateAnchors(anchors) }

        AnimatedVisibility(
            visible = viewState.shouldShowBottomPlayer,
            enter = slideInVertically(
                tween(600),
                initialOffsetY = { bottomPlayerHeightPx.roundToInt() * 2 }),
            exit = slideOutVertically(
                tween(600),
                targetOffsetY = { -bottomPlayerHeightPx.roundToInt() })
        ) {
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
                                orientation = Orientation.Vertical
                            ),
                        songModel = viewState.currentSong ?: SongModel(),
                        onPlayPausePressed = { viewModel.onAction(NowPlayingAction.PlayPressed) },
                        onNextPressed = { viewModel.onAction(NowPlayingAction.NextSongPressed) },
                        onPreviousPressed = { viewModel.onAction(NowPlayingAction.PreviousSongPressed) },
                        onProgressBarDragged = {
                            viewModel.onAction(
                                NowPlayingAction.ProgressBarDragged(
                                    it
                                )
                            )
                        },
                        onBackPress = { collapseBottomPlayer() },
                        playerState = viewState.currentPlayBackState,
                        songProgressProvider = viewModel::currentSongProgress,
                        songProgressMillisProvider = viewModel::currentSongProgressMillis
                    )
                }

                // BottomPlayer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .offset {
                            IntOffset(
                                x = 0,
                                y = bottomPlayerDragState
                                    .requireOffset()
                                    .toInt()
                            )
                        }
                        .graphicsLayer { alpha = alphaBottomPlayer },
                ) {
                    BottomPlayer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Utils.calculateBottomBarHeight())
                            .clickable { expandBottomPlayer() }
                            .anchoredDraggable(
                                state = bottomPlayerDragState,
                                orientation = Orientation.Vertical
                            ),
                        songProgressProvider = viewModel::currentSongProgress,
                        songModel = viewState.currentSong ?: SongModel(),
                        onPlayPausePressed = { viewModel.onAction(NowPlayingAction.PlayPressed) },
                        onNextPressed = { viewModel.onAction(NowPlayingAction.NextSongPressed) },
                        onPreviousPressed = { viewModel.onAction(NowPlayingAction.PreviousSongPressed) },
                        playerState = viewState.currentPlayBackState,
                    )
                }
            }
        }

    }
}

