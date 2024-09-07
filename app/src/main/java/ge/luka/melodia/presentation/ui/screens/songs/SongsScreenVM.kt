package ge.luka.melodia.presentation.ui.screens.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
class SongsScreenVM @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
) : ViewModel() {
    private val _songsList: MutableStateFlow<List<SongModel>> = MutableStateFlow(listOf())
    val songsList = _songsList.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mediaStoreRepository.getAllSongs().map { allSongs ->
                    _songsList.value = allSongs
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = listOf<SongModel>()
                )
            }
        }
    }
}