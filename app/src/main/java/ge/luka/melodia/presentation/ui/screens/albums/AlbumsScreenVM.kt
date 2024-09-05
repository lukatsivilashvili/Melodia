package ge.luka.melodia.presentation.ui.screens.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsScreenVM @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
) : ViewModel() {
    private val _albumsList: MutableStateFlow<List<AlbumModel>> = MutableStateFlow(listOf())
    val albumsList = _albumsList.asStateFlow()

    init {
        viewModelScope.launch {
            mediaStoreRepository.getAllAlbums().map { allalbums ->
                _albumsList.value = allalbums
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = listOf<SongModel>()
            )
        }
    }

}