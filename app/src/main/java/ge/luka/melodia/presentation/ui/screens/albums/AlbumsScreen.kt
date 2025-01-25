package ge.luka.melodia.presentation.ui.screens.albums

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ge.luka.melodia.common.mvi.CollectSideEffects
import ge.luka.melodia.common.utils.Base64Helper
import ge.luka.melodia.presentation.ui.components.shared.GeneralAlbumListItem
import ge.luka.melodia.presentation.ui.components.shared.MetadataDialog
import ge.luka.melodia.presentation.ui.screens.MelodiaScreen
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

    AlbumsScreenContent(
        modifier = modifier,
        artistId = artistId,
        artistName = artistName,
        navHostController = navHostController
    )
}

@Composable
fun AlbumsScreenContent(
    modifier: Modifier,
    viewModel: AlbumsScreenVM = hiltViewModel(),
    artistId: Long?,
    artistName: String?,
    navHostController: NavHostController,
) {
    val viewState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    CollectSideEffects(flow = viewModel.sideEffect) { effect ->
        when (effect) {
            is AlbumsSideEffect.ThrowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }

            is AlbumsSideEffect.UpdateCurrentAlbum -> {
                val updatedAlbum = viewState.currentEditingAlbum?.copy(
                    title = effect.title, artist = effect.artist, artUri = effect.artworkUri
                )
                if (updatedAlbum != null) {
                    viewModel.updateAlbumMetadata(updatedAlbum = updatedAlbum)
                } else {
                    Toast.makeText(context, "Album not found", Toast.LENGTH_SHORT).show()
                }
            }

            is AlbumsSideEffect.AlbumItemPressed -> {
                val encodedArtUri = Base64Helper.encodeToBase64(effect.albumArt)
                navHostController.navigate(
                    MelodiaScreen.AlbumSongs(
                        albumId = effect.albumId,
                        albumTitle = effect.albumTitle,
                        albumArtist = effect.albumArtist,
                        albumArt = encodedArtUri,
                        albumDuration = effect.albumDuration
                    )
                )
            }
        }
    }

    if (viewState.isDialogVisible && viewState.currentEditingAlbum != null) {
        MetadataDialog(audioModel = viewState.currentEditingAlbum,
            onDismiss = { viewModel.onAction(AlbumsAction.DialogDismiss) },
            shouldShowAlbumField = false,
            onSave = { id, title, artist, _, artworkUri ->
                viewModel.onAction(
                    AlbumsAction.MetadataSaved(
                        id = id, title = title, artist = artist, artworkUri = artworkUri
                    )
                )
                viewModel.onAction(AlbumsAction.DialogDismiss) // Close dialog after save
            })
    }

    LaunchedEffect(Unit) {
        if (artistId != null && artistName != null) {
            viewModel.getArtistAlbums(artistId = artistId)
        } else {
            viewModel.getAllAlbums()
        }
    }

    if (viewState.albumsList.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(), columns = GridCells.Fixed(2)
        ) {
            items(viewState.albumsList, key = { it.albumId ?: 0 }) { albumItem ->
                GeneralAlbumListItem(albumItem = albumItem, onClick = {
                    viewModel.onAction(
                        AlbumsAction.AlbumItemPressed(
                            albumId = albumItem.albumId ?: 0,
                            albumTitle = albumItem.title ?: "",
                            albumArtist = albumItem.artist ?: "",
                            albumArt = albumItem.artUri ?: "",
                            albumDuration = albumItem.duration ?: ""

                        )
                    )
                    Triple(
                        first = albumItem.title ?: "",
                        second = albumItem.albumId ?: 0,
                        third = albumItem
                    )
                }, onLongClick = {
                    viewModel.onAction(
                        AlbumsAction.AlbumLongPressed(album = albumItem)
                    )
                })
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = modifier.alpha(0.5F),
                text = "No Albums Found",
                style = MelodiaTypography.titleLarge,
            )
        }
    }
}