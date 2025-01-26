package ge.luka.melodia.presentation.ui.screens.nowplaying

sealed interface NowPlayingSideEffect {
    data class ProgressBarProgress(val progress: Float) : NowPlayingSideEffect
}