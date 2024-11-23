package ge.luka.melodia.presentation.ui.screens.songs

import BaseMviViewmodel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        }
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mediaStoreRepository.getAllSongs().map { allSongs ->
                    updateUiState { copy(songsList = allSongs) }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = listOf<SongModel>()
                )
            }
        }
    }
}