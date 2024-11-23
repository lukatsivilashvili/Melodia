import androidx.lifecycle.ViewModel
import ge.luka.melodia.common.extensions.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseMviViewmodel<UiState, UiAction, SideEffect>(
    initialUiState: UiState
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _sideEffect by lazy { Channel<SideEffect>() }
    val sideEffect: Flow<SideEffect> by lazy { _sideEffect.receiveAsFlow() }

    open fun onAction(uiAction: UiAction) = Unit

    protected fun updateUiState(newUiState: UiState) {
        _uiState.update { newUiState }
    }

    protected fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    protected fun ViewModel.emitSideEffect(effect: SideEffect) {
        // Main.immediate is used because of losing events issue: https://github.com/Kotlin/kotlinx.coroutines/issues/2886
        launch(Dispatchers.Main.immediate) {
            _sideEffect.send(effect)
        }
    }
}