package ge.luka.melodia.presentation.ui.screens.songs

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import ge.luka.melodia.presentation.ui.components.shared.GeneralMusicListItem
import ge.luka.melodia.presentation.ui.components.shared.HelperControlButtons
import ge.luka.melodia.presentation.ui.components.shared.MetadataDialog
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography

@Composable
fun SongsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {
    LaunchedEffect(Unit) {
        onUpdateRoute.invoke("Songs")
    }

    BackHandler {
        navHostController.popBackStack()
    }

    SongsScreenContent(modifier = modifier)
}

@Composable
fun SongsScreenContent(
    modifier: Modifier,
    viewModel: SongsScreenVM = hiltViewModel(),
) {

    val viewState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    CollectSideEffects(flow = viewModel.sideEffect) { effect ->
        when (effect) {
            is SongsSideEffect.ThrowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }

            is SongsSideEffect.UpdateCurrentSong -> {
                val updatedSong = viewState.currentEditingSong?.copy(
                    title = effect.title,
                    artist = effect.artist,
                    album = effect.album,
                    artUri = effect.artworkUri
                )
                if (updatedSong != null) {
                    viewModel.updateSongMetadata(updatedSong = updatedSong)
                } else {
                    Toast.makeText(context, "Song not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (viewState.isDialogVisible && viewState.currentEditingSong != null) {
        MetadataDialog(
            audioModel = viewState.currentEditingSong,
            onDismiss = { viewModel.onAction(SongsAction.DialogDismiss) },
            shouldShowAlbumField = true,
            onSave = { id, title, artist, album, artworkUri ->
                viewModel.onAction(
                    SongsAction.MetadataSaved(
                        id = id,
                        title = title,
                        artist = artist,
                        album = album ?: "",
                        artworkUri = artworkUri
                    )
                )
                viewModel.onAction(SongsAction.DialogDismiss) // Close dialog after save
            }
        )
    }

    if (viewState.songsList.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize(), state = rememberLazyListState()) {
            item {
                HelperControlButtons(
                    onPlayClick = { viewModel.onAction(SongsAction.PlayPressed) },
                    onShuffleClick = { viewModel.onAction(SongsAction.ShufflePressed) })
            }
            itemsIndexed(items = viewState.songsList, key = { _, song -> song.songId ?: 0 }) { index, songItem ->
                GeneralMusicListItem(
                    songItem = songItem,
                    onClick = {
                        viewModel.onAction(
                            SongsAction.SongPressed(
                                songs = viewState.songsList,
                                index = index
                            )
                        )
                    },
                    onLongClick = {
                        viewModel.onAction(
                            SongsAction.SongLongPressed(song = songItem)
                        )
                    }
                )
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