package ge.luka.melodia.presentation.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ge.luka.melodia.R
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography

@Composable
fun GeneralArtistListItem(
    modifier: Modifier = Modifier,
    artistItem: ArtistModel,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier
                .width(50.dp)
                .height(50.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        )
        {
            AsyncImage(
                placeholder = painterResource(id = R.drawable.ic_artist),
                error = painterResource(id = R.drawable.ic_artist),
                fallback = painterResource(id = R.drawable.ic_artist),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artistItem.artUri ?: R.drawable.ic_artist)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
            )
        }
        Column(
            modifier = modifier
        ) {
            Text(
                text = artistItem.title ?: "",
                fontSize = 14.sp,
                style = MelodiaTypography.labelLarge,
                fontWeight = FontWeight.SemiBold,

                modifier = modifier
                    .padding(start = 11.dp, end = 11.dp)
            )
        }
    }
}

@Preview
@Composable
fun ArtistItemPreview(modifier: Modifier = Modifier) {
    GeneralArtistListItem(
        modifier, ArtistModel(
            title = "A$/AP ROCKY",
            artistId = 0
        )
    )
}