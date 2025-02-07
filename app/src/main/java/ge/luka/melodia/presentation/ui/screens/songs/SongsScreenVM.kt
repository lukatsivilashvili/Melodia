package ge.luka.melodia.presentation.ui.screens.songs

import BaseMviViewmodel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import ge.luka.melodia.media3.PlayBackManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsScreenVM @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository,
    private val playbackManager: PlayBackManager
) : BaseMviViewmodel<SongsViewState, SongsAction, SongsSideEffect>(
    initialUiState = SongsViewState()
) {

    init {
        viewModelScope.launch {
            mediaStoreRepository.getAllSongs()
                .flowOn(Dispatchers.IO)
                .collect { allSongs ->
                    updateUiState { copy(songsList = allSongs) }
                }
        }
    }

    override fun onAction(uiAction: SongsAction) {
        when (uiAction) {
            is SongsAction.PlayPressed -> playbackManager.setPlaylistAndPlayAtIndex(uiAction.songs, 0)
            is SongsAction.ShufflePressed -> playbackManager.shufflePlaylist(uiAction.songs)
            is SongsAction.DialogDismiss -> updateUiState {
                copy(
                    isDialogVisible = false,
                    currentEditingSong = null
                )
            }

            is SongsAction.SongLongPressed -> updateUiState {
                copy(
                    isDialogVisible = true,
                    currentEditingSong = uiAction.song
                )
            }

            is SongsAction.SongPressed -> playbackManager.setPlaylistAndPlayAtIndex(uiAction.songs, uiAction.index)

            is SongsAction.MetadataSaved -> emitSideEffect(
                SongsSideEffect.UpdateCurrentSong(
                    id = uiAction.id,
                    title = uiAction.title,
                    artist = uiAction.artist,
                    album = uiAction.album,
                    artworkUri = uiAction.artworkUri
                )
            )
        }
    }

    fun updateSongMetadata(updatedSong: SongModel) {
        viewModelScope.launch {
            updateUiState {
                copy(
                    songsList = songsList.map { song ->
                        if (song.songId == updatedSong.songId) updatedSong else song
                    }
                )
            }
            val isSuccess = mediaStoreRepository.updateSongRecord(
                songId = updatedSong.songId ?: 0,
                title = updatedSong.title ?: "",
                artist = updatedSong.artist,
                album = updatedSong.album,
                artUri = updatedSong.artUri
            )
            if (isSuccess) {
                emitSideEffect(SongsSideEffect.ThrowToast("Metadata updated successfully"))
            } else {
                emitSideEffect(SongsSideEffect.ThrowToast("Failed to update metadata"))

            }
        }
    }
}