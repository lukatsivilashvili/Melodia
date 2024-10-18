package ge.luka.melodia.presentation.ui.screens.artistalbumsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.SongModel
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
class ArtistAlbumsScreenVM @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
) : ViewModel() {
    private val _artistAlbumsList: MutableStateFlow<List<AlbumModel>> = MutableStateFlow(listOf())
    val artistAlbumsList = _artistAlbumsList.asStateFlow()

    fun getArtistAlbums(artistId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mediaStoreRepository.getArtistAlbums(artistId = artistId).map { allAlbums ->
                    _artistAlbumsList.value = allAlbums
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = listOf<SongModel>()
                )
            }
        }

    }
}