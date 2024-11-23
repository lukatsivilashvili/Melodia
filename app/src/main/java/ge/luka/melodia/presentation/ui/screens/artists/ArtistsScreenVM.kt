package ge.luka.melodia.presentation.ui.screens.artists

import BaseMviViewmodel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArtistsScreenVM @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
) : BaseMviViewmodel<ArtistsViewState, ArtistsAction, ArtistsSideEffect>(
    initialUiState = ArtistsViewState()
) {

    override fun onAction(uiAction: ArtistsAction) {
        when (uiAction) {
            is ArtistsAction.OnArtistClicked -> emitSideEffect(
                ArtistsSideEffect.NavigateToAlbums(
                    artistId = uiAction.artistId,
                    artistName = uiAction.artistName
                )
            )
        }
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mediaStoreRepository.getAllArtists().map { allArtists ->
                    updateUiState { copy(artistsList = allArtists) }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = listOf<ArtistModel>()
                )
            }
        }
    }
}