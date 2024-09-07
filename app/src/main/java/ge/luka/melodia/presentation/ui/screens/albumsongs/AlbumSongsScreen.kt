package ge.luka.melodia.presentation.ui.screens.albumsongs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ge.luka.melodia.R
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.components.shared.GeneralMusicListItem
import ge.luka.melodia.presentation.ui.components.shared.HelperControlButtons
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography
import kotlinx.coroutines.launch

@Composable
fun AlbumSongsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit,
    albumId: Long? = null,
    albumModel: AlbumModel
) {
    val previousRoute =
        navHostController.previousBackStackEntry?.destination?.route?.getScreenFromRoute()


    BackHandler {
        onUpdateRoute.invoke(previousRoute)
        navHostController.popBackStack()
    }

    AlbumSongsScreenContent(albumModel = albumModel, albumId = albumId)
}

@Composable
fun AlbumSongsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: AlbumSongsScreenVM = hiltViewModel(),
    albumModel: AlbumModel,
    albumId: Long?
) {
    var songsList by remember { mutableStateOf(listOf<SongModel>()) }
    LaunchedEffect(Unit) {
        launch {
            viewModel.songsList.collect {
                songsList = it.filter { song -> song.albumId == albumId }
            }
        }
    }
    val derivedSongsList by remember {
        derivedStateOf { songsList }
    }

    if (derivedSongsList.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize(), state = rememberLazyListState()) {
            item {
                InfoBoxView(albumModel = albumModel)
                HelperControlButtons()
            }
            items(derivedSongsList, key = { it.songId ?: 0 }) { songItem ->
                GeneralMusicListItem(songItem = songItem)
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = modifier.alpha(0.5F),
                text = "No Songs Found",
                style = MelodiaTypography.titleLarge,
            )
        }
    }
}

@Composable
fun InfoBoxView(modifier: Modifier = Modifier, albumModel: AlbumModel) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = modifier
                .size(256.dp),
            placeholder = painterResource(id = R.drawable.ic_albums),
            error = painterResource(id = R.drawable.ic_albums),
            fallback = painterResource(id = R.drawable.ic_albums),
            contentScale = ContentScale.FillBounds,
            model = ImageRequest.Builder(LocalContext.current)
                .data(albumModel.artUri)
                .crossfade(true)
                .build(),
            contentDescription = null,
        )
        Text(
            text = albumModel.artist ?: "Artist",
            style = MelodiaTypography.labelSmall,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
        )
        Text(
            text = albumModel.title ?: "Album",
            fontWeight = FontWeight.SemiBold,
            style = MelodiaTypography.labelMedium,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = albumModel.duration ?: "Duration",
            style = MelodiaTypography.labelSmall,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AlbumSongsScreenPreview() {
    AlbumSongsScreenContent(
        albumModel = AlbumModel(
            albumId = 1,
            title = "Madvillainy",
            artist = "Madvillain",
            songCount = 99,
            artUri = "",
            duration = "4min"
        ), albumId = 1
    )
}

@Preview(showSystemUi = true)
@Composable
fun InfoBoxViewPreview(modifier: Modifier = Modifier) {
    InfoBoxView(
        modifier, AlbumModel(
            albumId = 1,
            title = "Madvillainy",
            artist = "Madvillain",
            songCount = 99,
            artUri = "",
            duration = "4min"
        )
    )
}