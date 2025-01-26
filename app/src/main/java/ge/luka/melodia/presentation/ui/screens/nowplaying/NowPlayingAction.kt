package ge.luka.melodia.presentation.ui.screens.nowplaying

sealed interface NowPlayingAction {
    data object PlayPressed : NowPlayingAction
    data object ShufflePressed : NowPlayingAction
    data object NextSongPressed : NowPlayingAction
    data object PreviousSongPressed : NowPlayingAction
    data object ProgressBarProgress : NowPlayingAction
    data class ProgressBarDragged(val progress: Float) : NowPlayingAction
}