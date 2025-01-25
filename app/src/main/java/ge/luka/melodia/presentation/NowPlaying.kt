package ge.luka.melodia.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.RepeatMode
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.components.bottomplayer.BarState
import ge.luka.melodia.presentation.ui.components.bottomplayer.BottomPlayer
import ge.luka.melodia.presentation.ui.components.bottomplayer.NowPlayingState

val BOTTOM_PLAYER_HEIGHT = 70.dp


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NowPlaying(
    modifier: Modifier = Modifier,
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
                BarState.COLLAPSED at 0f // Fully visible at bottom
                BarState.EXPANDED at -expandedOffset // Moves up by 48.dp
            }
        }
        val offset = -(bottomPlayerDragState.offset)
        val alpha = 1f - offset/(screenHeight.value*3.0f)
        SideEffect { bottomPlayerDragState.updateAnchors(anchors) }

        Box(
            modifier = Modifier
                .offset {
                    // Offset the BottomPlayer based on the drag state
                    IntOffset(
                        x = 0,
                        y = bottomPlayerDragState
                            .requireOffset()
                            .toInt()
                    )
                }
                .anchoredDraggable(
                    state = bottomPlayerDragState,
                    orientation = Orientation.Vertical
                )
                .alpha((alpha).coerceIn(0.0f, 1.0f))
        ) {

            // BottomPlayer Composable
            BottomPlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(BOTTOM_PLAYER_HEIGHT),
                nowPlayingState = NowPlayingState.Playing(
                    song = SongModel(
                        songId = 1L,
                        albumId = 101L,
                        artistId = 201L,
                        title = "Song One",
                        artist = "Artist One",
                        album = "Album One",
                        artUri = "",
                        duration = 210000L,
                        songPath = "/music/song_one.mp3",
                        bitrate = 320
                    ),
                    playbackState = PlayerState.PAUSED,
                    repeatMode = RepeatMode.NO_REPEAT,
                    isShuffleOn = false
                ),
                songProgressProvider = { 1f },
                enabled = true,
                onTogglePlayback = {},
                onNext = {},
                onPrevious = {}
            )
        }
    }
}