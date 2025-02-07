package ge.luka.melodia.presentation.ui.screens.albumsongs

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ge.luka.melodia.R
import ge.luka.melodia.common.mvi.CollectSideEffects
import ge.luka.melodia.common.utils.Base64Helper
import ge.luka.melodia.presentation.ui.components.shared.GeneralMusicListItem
import ge.luka.melodia.presentation.ui.components.shared.HelperControlButtons
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography

@Composable
fun AlbumSongsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit,
    albumId: Long,
    albumTitle: String,
    albumArtist: String,
    albumArt: String,
    albumDuration: String
) {

    LaunchedEffect(Unit) {
        onUpdateRoute.invoke(albumTitle)
    }

    BackHandler {
        navHostController.popBackStack()
    }

    AlbumSongsScreenContent(
        modifier = modifier,
        albumId = albumId,
        albumTitle = albumTitle,
        albumArtist = albumArtist,
        albumArt = albumArt,
        albumDuration = albumDuration
    )
}

@Composable
fun AlbumSongsScreenContent(
    modifier: Modifier,
    viewModel: AlbumSongsScreenVM = hiltViewModel(),
    albumId: Long,
    albumTitle: String,
    albumArtist: String,
    albumArt: String,
    albumDuration: String
) {
    val viewState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val decodedArtUri = Base64Helper.decodeFromBase64(albumArt)

    CollectSideEffects(flow = viewModel.sideEffect) { effect ->
        when (effect) {
            is AlbumSongsSideEffect.ThrowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAlbumSongs(albumId = albumId)
    }

    if (viewState.songsList.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize(), state = rememberLazyListState()) {
            item {
                InfoBoxView(
                    modifier = modifier,
                    albumTitle = albumTitle,
                    albumArtist = albumArtist,
                    albumArt = decodedArtUri,
                    albumDuration = albumDuration,
                )
                HelperControlButtons(onPlayClick = {
                    viewModel.onAction(
                        AlbumSongsAction.PlayPressed(
                            songs = viewState.songsList,
                        )
                    )
                },
                    onShuffleClick = {
                        viewModel.onAction(
                            AlbumSongsAction.ShufflePressed(
                                songs = viewState.songsList
                            )
                        )
                    })
            }
            itemsIndexed(
                items = viewState.songsList,
                key = { _, song -> song.songId ?: 0 }) { index, songItem ->
                GeneralMusicListItem(songItem = songItem, onClick = {
                    viewModel.onAction(
                        AlbumSongsAction.SongPressed(
                            songs = viewState.songsList,
                            index = index
                        )
                    )
                })
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
fun InfoBoxView(
    modifier: Modifier,
    albumTitle: String,
    albumArtist: String,
    albumArt: String,
    albumDuration: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = modifier
                .size(256.dp)
                .clip(RoundedCornerShape(10.dp)),
            placeholder = painterResource(id = R.drawable.ic_albums),
            error = painterResource(id = R.drawable.ic_albums),
            fallback = painterResource(id = R.drawable.ic_albums),
            contentScale = ContentScale.FillBounds,
            model = ImageRequest.Builder(LocalContext.current)
                .data(albumArt)
                .crossfade(true)
                .build(),
            contentDescription = null,
        )
        Text(
            text = albumArtist,
            style = MelodiaTypography.labelSmall,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
        )
        Text(
            text = albumTitle,
            fontWeight = FontWeight.SemiBold,
            style = MelodiaTypography.labelMedium,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = albumDuration,
            style = MelodiaTypography.labelSmall,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun AlbumSongsScreenPreview() {
//    AlbumSongsScreenContent(
//        modifier = Modifier,
//    )
//}
//
//@Preview(showSystemUi = true)
//@Composable
//fun InfoBoxViewPreview(modifier: Modifier = Modifier) {
//    InfoBoxView(
//        modifier, AlbumModel(
//            albumId = 1,
//            title = "Madvillainy",
//            artist = "Madvillain",
//            songCount = 99,
//            artUri = "",
//            duration = "4min"
//        )
//    )
//}