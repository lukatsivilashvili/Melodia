package ge.luka.melodia.presentation.ui.screens.albumsongs

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
class AlbumSongsScreenVM@Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
) : BaseMviViewmodel<AlbumSongsViewState, AlbumSongsAction, AlbumSongsSideEffect>(
    initialUiState = AlbumSongsViewState()
) {

    override fun onAction(uiAction: AlbumSongsAction) {
        when (uiAction) {
            is AlbumSongsAction.PlayPressed -> emitSideEffect(AlbumSongsSideEffect.ThrowToast("Play Pressed"))
            is AlbumSongsAction.ShufflePressed -> emitSideEffect(AlbumSongsSideEffect.ThrowToast("Shuffle Pressed"))
            is AlbumSongsAction.SongPressed -> emitSideEffect(AlbumSongsSideEffect.ThrowToast(uiAction.song.title ?: ""))
        }
    }

    fun getAlbumSongs(albumId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mediaStoreRepository.getAlbumSongs(albumId = albumId).map { albumSongs ->
                    updateUiState { copy(songsList = albumSongs) }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = listOf<SongModel>()
                )
            }
        }
    }
}