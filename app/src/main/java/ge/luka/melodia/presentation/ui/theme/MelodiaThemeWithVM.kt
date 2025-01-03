package ge.luka.melodia.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MelodiaThemeWithViewModel(
    viewModel: ThemeVM = hiltViewModel(),
    content: @Composable () -> Unit,
) {
    val viewState by viewModel.uiState.collectAsStateWithLifecycle()
    MelodiaTheme(viewState = viewState, content = content)
}