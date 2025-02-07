package ge.luka.melodia.presentation.ui.screens.nowplaying.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.components.shared.CrossFadingAlbumArt
import ge.luka.melodia.presentation.ui.components.shared.ErrorPainterType
import ge.luka.melodia.presentation.ui.theme.MelodiaTheme

@Composable
fun NowPlayingScreen(
    modifier: Modifier = Modifier,
    songModel: SongModel,
    playerState: PlayerState,
    songProgressProvider: () -> Float,
    songProgressMillisProvider: () -> Long,
    onPlayPausePressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onProgressBarDragged: (Float) -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        NowPlayingContent(
            songModel = songModel,
            playerState = playerState,
            onPlayPausePressed = onPlayPausePressed,
            onPreviousPressed = onPreviousPressed,
            onNextPressed = onNextPressed,
            onProgressBarDragged = onProgressBarDragged,
            songProgressProvider = songProgressProvider,
            songProgressMillisProvider = songProgressMillisProvider
        )
    }
}

@Composable
private fun NowPlayingContent(
    songModel: SongModel,
    playerState: PlayerState,
    songProgressProvider: () -> Float,
    songProgressMillisProvider: () -> Long,
    onPlayPausePressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onProgressBarDragged: (Float) -> Unit
) {
    val navBarHeight = with(LocalDensity.current) { WindowInsets.systemBars.getBottom(this).toDp() }
    val insets = WindowInsets.systemBars.asPaddingValues()
    val statusBarHeight = insets.calculateTopPadding()


    val containerModifier =
        Modifier
            .padding(bottom = navBarHeight + statusBarHeight)
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                RoundedCornerShape(bottomEnd = 60.dp, bottomStart = 60.dp),
            )
            .clip(RoundedCornerShape(bottomEnd = 60.dp, bottomStart = 60.dp))
            .clipToBounds()

    Box(modifier = containerModifier) {
        Column {
            AlbumArtSection(
                modifier = Modifier.weight(1f),
                songModel = songModel
            )  // Pass only weight modifier
            Controls(
                songModel = songModel,
                playerState = playerState,
                onPlayPausePressed = onPlayPausePressed,
                onPreviousPressed = onPreviousPressed,
                onNextPressed = onNextPressed,
                onProgressBarDragged = onProgressBarDragged,
                songProgressProvider = songProgressProvider,
                songProgressMillisProvider = songProgressMillisProvider
            )
        }
    }
}

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
private fun AlbumArtSection(
    modifier: Modifier = Modifier,
    songModel: SongModel
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CrossFadingAlbumArt(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f),
            artUri = songModel.artUri ?: "",
            errorPainterType = ErrorPainterType.SOLID_COLOR,
        )
        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surfaceContainer
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
        // Foreground Album Art
        CrossFadingAlbumArt(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            artUri = songModel.artUri ?: "",
            errorPainterType = ErrorPainterType.PLACEHOLDER,
        )
    }
}

@Preview
@Composable
fun NowPlayingScreenPreview() {
    MelodiaTheme {
        NowPlayingScreen(
            songModel = SongModel(),
            onPlayPausePressed = {},
            onPreviousPressed = {},
            onNextPressed = {},
            onProgressBarDragged = {},
            playerState = PlayerState.PAUSED,
            songProgressProvider = { 0.0f },
            songProgressMillisProvider = { 0L }
        )
    }
}