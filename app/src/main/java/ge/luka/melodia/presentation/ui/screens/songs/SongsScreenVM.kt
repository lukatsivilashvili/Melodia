package ge.luka.melodia.presentation.ui.screens.songs

import BaseMviViewmodel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsScreenVM @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
) : BaseMviViewmodel<SongsViewState, SongsAction, SongsSideEffect>(
    initialUiState = SongsViewState()
) {

    override fun onAction(uiAction: SongsAction) {
        when (uiAction) {
            is SongsAction.PlayPressed -> emitSideEffect(SongsSideEffect.ThrowToast("Play Pressed"))
            is SongsAction.ShufflePressed -> emitSideEffect(SongsSideEffect.ThrowToast("Shuffle Pressed"))
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

            is SongsAction.SongPressed -> emitSideEffect(
                SongsSideEffect.ThrowToast(
                    uiAction.song.title ?: ""
                )
            )

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
            try {
                updateUiState {
                    copy(
                        songsList = songsList.map { song ->
                            if (song.songId == updatedSong.songId) updatedSong else song
                        }
                    )
                }
                mediaStoreRepository.updateSongRecord(
                    songId = updatedSong.songId ?: 0,
                    title = updatedSong.title ?: "",
                    artist = updatedSong.artist,
                    album = updatedSong.album,
                    artUri = updatedSong.artUri
                )
                emitSideEffect(SongsSideEffect.ThrowToast("Metadata updated successfully"))
            } catch (e: Exception) {
                emitSideEffect(SongsSideEffect.ThrowToast("Failed to update metadata"))
            }
        }
    }

    init {
        viewModelScope.launch {
            mediaStoreRepository.getAllSongs()
                .flowOn(Dispatchers.IO)
                .collect { allSongs ->
                    updateUiState { copy(songsList = allSongs) }
                }
        }
    }
}