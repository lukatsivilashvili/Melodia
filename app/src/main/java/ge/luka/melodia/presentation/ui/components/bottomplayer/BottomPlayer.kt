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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.components.shared.CrossFadingAlbumArt
import ge.luka.melodia.presentation.ui.components.shared.ErrorPainterType
import ge.luka.melodia.presentation.ui.components.shared.TransparentSurface
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


@Composable
fun BottomPlayer(
    modifier: Modifier,
    songProgressProvider: () -> Float,
    songModel: SongModel,
    playerState: PlayerState,
    onPlayPausePressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
) {
    TransparentSurface {
        Box {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CrossFadingAlbumArt(
                    modifier = Modifier
                        .fillMaxHeight(fraction = 0.7f)
                        .padding(8.dp)
                        .aspectRatio(1.0f)
                        .clip(CircleShape),
                    artUri = songModel.artUri ?: "",
                    errorPainterType = ErrorPainterType.PLACEHOLDER,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.basicMarquee(),
                        text = songModel.title ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        modifier = Modifier,
                        text = songModel.artist.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row {
                    IconButton(onClick = onPreviousPressed) {
                        Icon(
                            imageVector = Icons.TwoTone.SkipPrevious,
                            contentDescription = "Previous"
                        )
                    }
                    Box(contentAlignment = Alignment.Center) {
                        IconButton(
                            modifier = Modifier.padding(end = 4.dp),
                            onClick = onPlayPausePressed
                        ) {
                            val icon =
                                if (playerState == PlayerState.PLAYING) Icons.TwoTone.Pause else Icons.TwoTone.PlayArrow
                            Icon(imageVector = icon, contentDescription = null)
                        }
                        SongCircularProgressIndicator(
                            modifier = Modifier.padding(end = 4.dp),
                            songProgressProvider
                        )
                    }
                    IconButton(onClick = onNextPressed) {
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
            .fillMaxWidth(),
        songProgressProvider = { 1f },
        songModel = SongModel(),
        playerState = PlayerState.PAUSED,
        onPlayPausePressed = {},
        onPreviousPressed = {},
        onNextPressed = {},
    )
}
