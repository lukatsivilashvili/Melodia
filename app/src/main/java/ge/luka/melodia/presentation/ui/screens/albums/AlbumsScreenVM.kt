package ge.luka.melodia.presentation.ui.screens.albums

import BaseMviViewmodel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.model.AlbumModel
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
            is AlbumsAction.AlbumItemPressed -> emitSideEffect(
                AlbumsSideEffect.AlbumItemPressed(
                    title = uiAction.title,
                    albumId = uiAction.albumId,
                    albumModel = uiAction.albumModel
                )
            )

            is AlbumsAction.MetadataSaved -> emitSideEffect(
                AlbumsSideEffect.UpdateCurrentAlbum(
                    id = uiAction.id,
                    title = uiAction.title,
                    artist = uiAction.artist,
                    artworkUri = uiAction.artworkUri
                )
            )

            AlbumsAction.DialogDismiss -> updateUiState {
                copy(
                    isDialogVisible = false,
                    currentEditingAlbum = null
                )
            }

            is AlbumsAction.AlbumLongPressed -> updateUiState {
                copy(
                    isDialogVisible = true,
                    currentEditingAlbum = uiAction.album
                )
            }
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
                    initialValue = listOf<AlbumModel>()
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

    fun updateAlbumMetadata(updatedAlbum: AlbumModel) {
        viewModelScope.launch {
            updateUiState {
                copy(
                    albumsList = albumsList.map { album ->
                        if (album.albumId == updatedAlbum.albumId) updatedAlbum else album
                    })
            }
            val isSuccess = mediaStoreRepository.updateAlbumRecord(
                artistId = updatedAlbum.artistId ?: 0,
                albumId = updatedAlbum.albumId ?: 0,
                title = updatedAlbum.title ?: "",
                artist = updatedAlbum.artist,
                artUri = updatedAlbum.artUri
            )
            if (isSuccess) {
                emitSideEffect(AlbumsSideEffect.ThrowToast("Metadata updated successfully"))
            } else {
                emitSideEffect(AlbumsSideEffect.ThrowToast("Failed to update metadata"))
            }
        }
    }
}