package ge.luka.melodia.presentation.ui.screens.nowplaying

import BaseMviViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.luka.melodia.media3.PlayBackManager
import javax.inject.Inject

@HiltViewModel
class NowPlayingVM @Inject constructor(
    private val playBackManager: PlayBackManager
) : BaseMviViewmodel<NowPlayingViewState, NowPlayingAction, NowPlayingSideEffect>(
    initialUiState = NowPlayingViewState()
) {
    override fun onAction(uiAction: NowPlayingAction) {
        when (uiAction) {
            NowPlayingAction.PlayPressed -> playBackManager.togglePlayback()
            NowPlayingAction.NextSongPressed -> playBackManager.playNextSong()
            NowPlayingAction.PreviousSongPressed -> playBackManager.playPreviousSong()
            NowPlayingAction.ShufflePressed -> {}
            NowPlayingAction.ProgressBarProgress -> emitSideEffect(
                NowPlayingSideEffect.ProgressBarProgress(
                    playBackManager.currentSongProgress
                )
            )

            is NowPlayingAction.ProgressBarDragged -> playBackManager.seekToPosition(progress = uiAction.progress)
        }
    }

}