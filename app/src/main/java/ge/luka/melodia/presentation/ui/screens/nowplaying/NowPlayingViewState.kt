package ge.luka.melodia.presentation.ui.screens.nowplaying

import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.SongModel

data class NowPlayingViewState(
    val currentSong: SongModel? = null,
    val currentPlayBackState: PlayerState = PlayerState.PAUSED,
)
