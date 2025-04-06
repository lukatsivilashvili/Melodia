package ge.luka.melodia.presentation.ui.components.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ge.luka.melodia.R
import ge.luka.melodia.common.extensions.formatDuration
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GeneralMusicListItem(
    modifier: Modifier = Modifier,
    songItem: SongModel,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)
            .combinedClickable(
                onClick = { onClick.invoke() },
                onLongClick = { onLongClick.invoke() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier
                .width(50.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center
        )
        {
            AsyncImage(
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_songs),
                error = painterResource(id = R.drawable.ic_songs),
                fallback = painterResource(id = R.drawable.ic_songs),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(songItem.artUri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
            )
        }
        Column(
            modifier = modifier
                .weight(1f)
        ) {
            Text(
                text = songItem.title ?: "",
                fontSize = 14.sp,
                style = MelodiaTypography.labelLarge,
                fontWeight = FontWeight.SemiBold,

                modifier = modifier
                    .padding(start = 11.dp, end = 11.dp)
            )
            Text(
                text = songItem.artist ?: "",
                fontSize = 12.sp,
                modifier = modifier
                    .padding(start = 11.dp, end = 11.dp),
                style = MelodiaTypography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = songItem.duration?.formatDuration() ?: "00:00",
            fontSize = 10.sp,
            modifier = modifier
                .padding(start = 22.dp),
            style = MelodiaTypography.labelLarge
        )
    }
}