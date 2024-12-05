package ge.luka.melodia.presentation.ui.components

import BaseMviViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import ge.luka.melodia.presentation.ui.components.singlepermission.PermissionAction
import ge.luka.melodia.presentation.ui.components.singlepermission.PermissionSideEffect
import ge.luka.melodia.presentation.ui.components.singlepermission.PermissionViewState
import javax.inject.Inject

@HiltViewModel
class SinglePermissionViewModel @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
) : BaseMviViewmodel<PermissionViewState, PermissionAction, PermissionSideEffect>(
    initialUiState = PermissionViewState()
) {

    override fun onAction(uiAction: PermissionAction) {
        when (uiAction) {
            is PermissionAction.PermissionGranted -> emitSideEffect(PermissionSideEffect.PermissionGranted)
        }
    }

    suspend fun cacheData() {
        mediaStoreRepository.cacheAllSongs()
        mediaStoreRepository.cacheAllArtists()
        mediaStoreRepository.cacheAllAlbums()
    }
}