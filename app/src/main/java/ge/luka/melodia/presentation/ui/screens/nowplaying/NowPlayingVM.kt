package ge.luka.melodia.presentation.ui.screens.nowplaying

import BaseMviViewmodel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.media3.PlayBackManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NowPlayingVM @Inject constructor(
    private val playBackManager: PlayBackManager
) : BaseMviViewmodel<NowPlayingViewState, NowPlayingAction, NowPlayingSideEffect>(
    initialUiState = NowPlayingViewState()
) {

    init {
        playBackManager.state
            .map { mediaPlayerState ->
                val song = mediaPlayerState.currentPlayingSong
                    ?: return@map null
                updateUiState {
                    copy(currentSong = song, currentPlayBackState = mediaPlayerState.playbackState.playerState)
                }
            }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    }

    override fun onAction(uiAction: NowPlayingAction) {
        when (uiAction) {
            NowPlayingAction.PlayPressed -> {
                playBackManager.togglePlayback()
            }
            NowPlayingAction.NextSongPressed -> playBackManager.playNextSong()
            NowPlayingAction.PreviousSongPressed -> playBackManager.playPreviousSong()
            NowPlayingAction.ShufflePressed -> {}
            is NowPlayingAction.ProgressBarDragged -> playBackManager.seekToPosition(progress = uiAction.progress)
        }
    }

    fun currentSongProgress() = playBackManager.currentSongProgress
    fun currentSongProgressMillis() = playBackManager.currentSongProgressMillis
}