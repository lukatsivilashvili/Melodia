package ge.luka.melodia.presentation.ui.screens.artistalbumsscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.presentation.ui.MelodiaScreen
import ge.luka.melodia.presentation.ui.components.shared.GeneralAlbumListItem
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography
import kotlinx.coroutines.launch

@Composable
fun ArtistAlbumsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit,
    artistId: Long,
    artistName: String,
) {
    LaunchedEffect(Unit) {
        onUpdateRoute.invoke(artistName)
    }

    BackHandler {
        navHostController.popBackStack()
    }

    ArtistAlbumsScreenContent(artistId = artistId) {
        navHostController.navigate(MelodiaScreen.AlbumSongs(it.second, it.third))
    }
}

@Composable
fun ArtistAlbumsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: ArtistAlbumsScreenVM = hiltViewModel(),
    artistId: Long,
    onClick: (Triple<String, Long, AlbumModel>) -> Unit
) {
    var albumsList by remember { mutableStateOf(listOf<AlbumModel>()) }

    LaunchedEffect(Unit) {
        launch { viewModel.getArtistAlbums(artistId = artistId) }
        launch { viewModel.artistAlbumsList.collect { albumsList = it } }
    }

    val derivedSongsList by remember {
        derivedStateOf { albumsList }
    }

    if (derivedSongsList.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2)
        ) {
            items(derivedSongsList, key = { it.albumId ?: 0 }) { albumItem ->
                GeneralAlbumListItem(albumItem = albumItem, modifier = modifier.clickable {
                    onClick.invoke(
                        Triple(
                            first = albumItem.title ?: "",
                            second = albumItem.albumId ?: 0,
                            third = albumItem
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
                text = "No Albums Found",
                style = MelodiaTypography.titleLarge,
            )
        }
    }
}