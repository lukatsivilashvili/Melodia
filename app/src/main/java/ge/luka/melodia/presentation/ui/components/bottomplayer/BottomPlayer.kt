package ge.luka.melodia.presentation.ui.components.bottomplayer


import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Pause
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material.icons.twotone.SkipNext
import androidx.compose.material.icons.twotone.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ge.luka.melodia.R
import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.RepeatMode
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.components.shared.TransparentSurface
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


@Composable
fun BottomPlayer(
    modifier: Modifier,
    nowPlayingState: NowPlayingState,
    songProgressProvider: () -> Float,
    statusBarHeight: Dp,
    enabled: Boolean,
    onTogglePlayback: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
) {
    if (nowPlayingState is NowPlayingState.NotPlaying) {
        return
    }
    val state = (nowPlayingState as NowPlayingState.Playing)
    val song = state.song
    TransparentSurface {
        Box {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(bottom = statusBarHeight), // Add bottom padding to shift content up
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                        .aspectRatio(1.0f)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_albums),
                    error = painterResource(id = R.drawable.ic_albums),
                    fallback = painterResource(id = R.drawable.ic_albums),
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(song.artUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "AlbumCover",
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                        text = song.title ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        modifier = Modifier,
                        text = song.artist.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row {
                    IconButton(onClick = onPrevious, enabled = enabled) {
                        Icon(
                            imageVector = Icons.TwoTone.SkipPrevious,
                            contentDescription = "Previous"
                        )
                    }
                    Box(contentAlignment = Alignment.Center) {
                        IconButton(
                            modifier = Modifier.padding(end = 4.dp),
                            onClick = onTogglePlayback,
                            enabled = enabled
                        ) {
                            val icon =
                                if (state.playbackState == PlayerState.PLAYING) Icons.TwoTone.Pause else Icons.TwoTone.PlayArrow
                            Icon(imageVector = icon, contentDescription = null)
                        }
                        SongCircularProgressIndicator(
                            modifier = Modifier.padding(end = 4.dp),
                            songProgressProvider
                        )
                    }
                    IconButton(onClick = onNext, enabled = enabled) {
                        Icon(imageVector = Icons.TwoTone.SkipNext, contentDescription = "Next")
                    }
                }
            }
            // Add a Spacer at the bottom to create empty space
            Spacer(modifier = Modifier.height(32.dp)) // Adjust the height as needed
        }
    }
}

@Composable
fun SongCircularProgressIndicator(
    modifier: Modifier,
    songProgressProvider: () -> Float,
) {
    val progress = remember {
        Animatable(0.0f)
    }
    LaunchedEffect(key1 = Unit) {
        while (isActive) {
            val newProgress = songProgressProvider()
            progress.animateTo(newProgress)
            delay(1000)
        }
    }
    CircularProgressIndicator(
        progress = { progress.value },
        modifier = modifier,
        strokeCap = StrokeCap.Round,
        strokeWidth = 2.dp,
        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    )
}

enum class BarState {
    COLLAPSED, EXPANDED
}

@Preview
@Composable
fun BottomPlayerPreview(modifier: Modifier = Modifier) {
    BottomPlayer(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
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
        onPrevious = {},
        statusBarHeight = 16.dp
    )
}
