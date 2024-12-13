package ge.luka.melodia.presentation.ui.screens.songs

import BaseMviViewmodel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
            is SongsAction.SongPressed -> emitSideEffect(SongsSideEffect.ThrowToast(uiAction.song.title ?: ""))
            is SongsAction.SongLongPressed -> updateUiState { copy(isDialogVisible = true, currentEditingSong = uiAction.song) }
            is SongsAction.DialogDismiss -> updateUiState { copy(isDialogVisible = false, currentEditingSong = null) }
            is SongsAction.MetadataSaved -> emitSideEffect(SongsSideEffect.ThrowToast("Saved"))

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