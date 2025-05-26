package ge.luka.melodia.presentation.ui.screens.nowplaying.components

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ge.luka.melodia.common.utils.WindowType
import ge.luka.melodia.common.utils.rememberWindowSize
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
    onProgressBarDragged: (Float) -> Unit,
    onBackPress: () -> Unit,
    palette: Map<String, String>
) {
    BackHandler {
        onBackPress.invoke()
    }

    NowPlayingContent(
        modifier = modifier.fillMaxSize(),
        songModel = songModel,
        playerState = playerState,
        songProgressProvider = songProgressProvider,
        songProgressMillisProvider = songProgressMillisProvider,
        onPlayPausePressed = onPlayPausePressed,
        onPreviousPressed = onPreviousPressed,
        onNextPressed = onNextPressed,
        onProgressBarDragged = onProgressBarDragged,
        palette = palette
    )
}

@Composable
private fun NowPlayingContent(
    modifier: Modifier = Modifier,
    songModel: SongModel,
    playerState: PlayerState,
    songProgressProvider: () -> Float,
    songProgressMillisProvider: () -> Long,
    onPlayPausePressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onProgressBarDragged: (Float) -> Unit,
    palette: Map<String, String>
) {

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize()
    ) {
        CrossFadingAlbumArt(
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 100.dp)
                .alpha(0.6f),
            artUri = songModel.artUri.orEmpty(),
            errorPainterType = ErrorPainterType.SOLID_COLOR,
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Add any new components here above the album art
            // Example: TopBar(), SearchBar(), etc.

            // Album art section that adapts to remaining space
            Box(
                modifier = Modifier
                    .weight(1f, fill = false) // Don't fill all space, just take what's needed
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AlbumArtSection(
                    modifier = Modifier.fillMaxSize(),
                    songModel = songModel
                )
            }

            Controls(
                modifier = Modifier.padding(bottom = 24.dp),
                songModel = songModel,
                playerState = playerState,
                songProgressProvider = songProgressProvider,
                songProgressMillisProvider = songProgressMillisProvider,
                onPlayPausePressed = onPlayPausePressed,
                onPreviousPressed = onPreviousPressed,
                onNextPressed = onNextPressed,
                onProgressBarDragged = onProgressBarDragged,
                palette = palette
            )

            // Add any new components here within Controls section
            // They will be positioned below the main controls
        }
    }
}

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
private fun AlbumArtSection(modifier: Modifier = Modifier, songModel: SongModel) {
    val windowSize = rememberWindowSize()
    val imagePadding = when (windowSize) {
        WindowType.Compact -> 60.dp
        WindowType.Medium -> 30.dp
        WindowType.Expanded -> 15.dp
    }

    // Use BoxWithConstraints to get actual available space
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Calculate the maximum size based on available width and height
        val maxSize = minOf(maxWidth, maxHeight) - (imagePadding * 2)

        CrossFadingAlbumArt(
            modifier = Modifier
                .size(maxSize)
                .clip(RoundedCornerShape(8.dp)),
            artUri = songModel.artUri.orEmpty(),
            errorPainterType = ErrorPainterType.PLACEHOLDER,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun NowPlayingScreenPreview() {
    MelodiaTheme {
        NowPlayingScreen(
            songModel = SongModel(),
            playerState = PlayerState.PAUSED,
            songProgressProvider = { 0f },
            songProgressMillisProvider = { 0L },
            onPlayPausePressed = {},
            onPreviousPressed = {},
            onNextPressed = {},
            onProgressBarDragged = {},
            onBackPress = {},
            palette = emptyMap()
        )
    }
}
