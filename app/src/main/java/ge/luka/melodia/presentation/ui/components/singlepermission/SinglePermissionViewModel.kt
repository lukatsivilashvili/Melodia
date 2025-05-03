package ge.luka.melodia.presentation.ui.components.singlepermission

import BaseMviViewmodel
import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.data.MediaStoreLoader
import ge.luka.melodia.domain.repository.MediaStoreRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SinglePermissionViewModel @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository,
    private val mediaStoreLoader: MediaStoreLoader,
) : BaseMviViewmodel<PermissionViewState, PermissionAction, PermissionSideEffect>(
    initialUiState = PermissionViewState(scanningState = emptyList(), scanningFinished = false)
) {

    override fun onAction(uiAction: PermissionAction) {
        when (uiAction) {
            is PermissionAction.PermissionGranted -> emitSideEffect(
                PermissionSideEffect.PermissionGranted(
                    uiAction.folderUri
                )
            )
        }
    }

    fun startScan(context: Context, folderUri: String?) {
        viewModelScope.launch {
            mediaStoreLoader.setSelectedFolderUri(folderUri)
            mediaStoreLoader.scanSongsList(context).collect { song ->
                mediaStoreRepository.cacheSong(song)
                updateUiState { copy(scanningState = scanningState + song) }
            }
            mediaStoreRepository.cacheAllAlbums()
            mediaStoreRepository.cacheAllArtists()
            updateUiState { copy(scanningFinished = true) }
        }
    }
}