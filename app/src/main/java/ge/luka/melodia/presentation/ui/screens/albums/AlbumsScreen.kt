package ge.luka.melodia.presentation.ui.screens.albums

import android.widget.Toast
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ge.luka.melodia.common.mvi.CollectSideEffects
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.presentation.ui.MelodiaScreen
import ge.luka.melodia.presentation.ui.components.shared.GeneralAlbumListItem
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography

@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit,
    artistId: Long?,
    artistName: String?,
) {

    LaunchedEffect(Unit) {
        if (artistName != null) onUpdateRoute.invoke(artistName) else onUpdateRoute.invoke("Albums")
    }

    BackHandler {
        navHostController.popBackStack()
    }

    AlbumsScreenContent(modifier = modifier, artistId = artistId, artistName = artistName) {
        navHostController.navigate(MelodiaScreen.AlbumSongs(it.second, it.third))
    }
}

@Composable
fun AlbumsScreenContent(
    modifier: Modifier,
    viewModel: AlbumsScreenVM = hiltViewModel(),
    artistId: Long?,
    artistName: String?,
    onClick: (Triple<String, Long, AlbumModel>) -> Unit
) {
    val viewState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    CollectSideEffects(flow = viewModel.sideEffect) { effect ->
        when (effect) {
            is AlbumsSideEffect.ThrowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    LaunchedEffect(Unit) {
        if (artistId != null && artistName != null) {
            viewModel.getArtistAlbums(artistId = artistId)
        } else {
            viewModel.getAllAlbums()
        }
    }

    val derivedSongsList by remember {
        derivedStateOf { viewState.albumsList }
    }

    if (derivedSongsList.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2)
        ) {
            items(derivedSongsList, key = { it.albumId ?: 0 }) { albumItem ->
                GeneralAlbumListItem(albumItem = albumItem, modifier = modifier.clickable {
                    viewModel.onAction(AlbumsAction.AlbumPressed(albumItem))
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