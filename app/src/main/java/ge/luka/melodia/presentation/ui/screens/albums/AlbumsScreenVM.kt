package ge.luka.melodia.presentation.ui.screens.albums

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
class AlbumsScreenVM @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
) : BaseMviViewmodel<AlbumsViewState, AlbumsAction, AlbumsSideEffect>(initialUiState = AlbumsViewState()) {

    override fun onAction(uiAction: AlbumsAction) {
        when (uiAction) {
            is AlbumsAction.AlbumPressed -> emitSideEffect(
                AlbumsSideEffect.ThrowToast(
                    uiAction.album.title ?: ""
                )
            )
        }
    }

    fun getAllAlbums() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mediaStoreRepository.getAllAlbums().map { allAlbums ->
                    updateUiState { copy(albumsList = allAlbums) }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = listOf<SongModel>()
                )
            }
        }
    }

    fun getArtistAlbums(artistId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mediaStoreRepository.getArtistAlbums(artistId = artistId).map { allAlbums ->
                    updateUiState { copy(albumsList = allAlbums) }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = listOf<SongModel>()
                )
            }
        }
    }

}