package ge.luka.melodia.presentation.ui.components.bottomplayer

import androidx.compose.runtime.Immutable
import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.RepeatMode
import ge.luka.melodia.domain.model.SongModel

@Immutable
sealed interface NowPlayingState {


    @Immutable
    data object NotPlaying : NowPlayingState

    @Immutable
    data class Playing(
        val song: SongModel,
        val playbackState: PlayerState,
        val repeatMode: RepeatMode,
        val isShuffleOn: Boolean,
    ) : NowPlayingState

}
