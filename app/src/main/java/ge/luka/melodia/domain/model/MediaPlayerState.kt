package ge.luka.melodia.domain.model

data class MediaPlayerState(
    val currentPlayingSong: SongModel?,
    val playbackState: PlaybackState
) {
    companion object {
        val empty = MediaPlayerState(null, PlaybackState.emptyState)
    }
}
