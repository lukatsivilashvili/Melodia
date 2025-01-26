package ge.luka.melodia.presentation.ui.screens.nowplaying

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ge.luka.melodia.R
import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.screens.nowplaying.components.Controls
import ge.luka.melodia.presentation.ui.theme.MelodiaTheme

@Composable
fun NowPlayingScreen(modifier: Modifier = Modifier) {

    Box(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)) {
        NowPlayingContent()
    }
}

@Composable
private fun NowPlayingContent() {
    val navBarHeight = with(LocalDensity.current) { WindowInsets.systemBars.getBottom(this).toDp() }
    var height by remember { mutableIntStateOf(0) }
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
            AlbumArtSection(Modifier.weight(1f))  // Pass only weight modifier
            Controls(
                activeItem = SongModel(title = "Song One", artist = "Lil Uzi Vert"),
                playerState = PlayerState.PLAYING
            )
        }
    }
}

@Composable
private fun AlbumArtSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background Image with Blur and Gradient Overlay
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("content://media/external/audio/albumart/831838724258952365")
                .crossfade(true)
                .build(),
            contentDescription = "Blurred Album Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f)
        )
        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.surfaceContainer),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
        // Foreground Album Art
        AsyncImage(
            modifier = modifier
                .fillMaxSize()
                .padding(48.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data("content://media/external/audio/albumart/831838724258952365")
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_albums),
            error = painterResource(id = R.drawable.ic_albums),
            fallback = painterResource(id = R.drawable.ic_albums),
            contentDescription = "Album Cover",
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview
@Composable
fun NowPlayingScreenPreview() {
    MelodiaTheme {
        NowPlayingScreen()
    }
}