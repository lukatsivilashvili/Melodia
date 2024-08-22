package ge.luka.melodia.presentation.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    dataStoreRepository: DataStoreRepository
) : ViewModel() {

    // Observe the DataStore flow for dynamic theme preference
    val isDarkMode: StateFlow<Boolean> =
        dataStoreRepository.getDarkMode().map { it }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    // Observe the DataStore flow for theme type preference
    val currentTheme: StateFlow<AppTheme> =
        dataStoreRepository.getCurrentTheme().map { it }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppTheme.GREEN
        )
}