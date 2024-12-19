package ge.luka.melodia.presentation.ui.components.singlepermission

import BaseMviViewmodel
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.common.utils.FileHelper
import ge.luka.melodia.domain.repository.MediaStoreRepository
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
        mediaStoreRepository.cacheAllAlbums()
        mediaStoreRepository.cacheAllArtists()
    }

    fun createMediaDirectory(context: Context) {
        FileHelper.createAppDirectory(context = context, folderName = "MelodiaMusic")
    }
}