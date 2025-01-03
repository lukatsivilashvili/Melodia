package ge.luka.melodia.presentation.ui.screens.nowplaying.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.SongModel

@Composable
fun Controls(
    modifier: Modifier = Modifier,
    activeItem: SongModel,
    playerState: PlayerState,
) {
    var showRemaining by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(0f) }
    var dragging by remember { mutableStateOf(false) }
    val nextPrevColors = IconButtonDefaults.filledIconButtonColors(
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
    )
    Column(modifier.padding(start = 30.dp, end = 30.dp, bottom = 30.dp)) {
        Text(
            text = activeItem.title ?: "",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = activeItem.artist ?: "",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth(),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        Spacer(Modifier.height(24.dp))
        Box(Modifier.fillMaxWidth()) {
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value =sliderValue,
                onValueChange = {
                    sliderValue = it
                    dragging = true
                },
                onValueChangeFinished = {
                    dragging = false
                },
            )
        }
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextButton(
                onClick = { },
                contentPadding = PaddingValues(4.dp),
                colors =
                ButtonDefaults.textButtonColors(
                    contentColor =
                    animateColorAsState(
                        targetValue =
                        if (dragging) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        animationSpec = tween(),
                        label = "",
                    ).value,
                ),
            ) {
                Text(
                    text ="3:23",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Row(
            modifier =
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = true) {
                FilledIconButton(
                    onClick = { },
                    modifier = Modifier.size(80.dp),
                    colors = nextPrevColors,
                ) {
                    Icon(
                        Icons.Outlined.SkipPrevious,
                        null,
                        modifier = Modifier.fillMaxSize(.5f),
                    )
                }
            }
            FilledIconButton(
                onClick = { },
                modifier =
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .weight(1f),
                shape = CircleShape,
                colors =
                IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primaryContainer,
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Icon(
                    if (true) {
                        Icons.Default.Pause
                    } else {
                        Icons.Default.PlayArrow
                    },
                    null,
                    modifier = Modifier.size(40.dp),
                )
            }
            AnimatedVisibility(visible = true) {
                FilledIconButton(
                    onClick = {  },
                    modifier = Modifier.size(80.dp),
                    colors = nextPrevColors,
                ) {
                    Icon(
                        Icons.Outlined.SkipNext,
                        null,
                        modifier = Modifier.fillMaxSize(.5f),
                    )
                }
            }
        }
    }
}