package ge.luka.melodia.presentation.ui.screens.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArtistsScreenVM @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
) : ViewModel() {
    private val _artistsList: MutableStateFlow<List<ArtistModel>> = MutableStateFlow(listOf())
    val artistsList = _artistsList.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mediaStoreRepository.getAllArtists().map { allArtists ->
                    _artistsList.value = allArtists
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = listOf<ArtistModel>()
                )
            }
        }
    }
}