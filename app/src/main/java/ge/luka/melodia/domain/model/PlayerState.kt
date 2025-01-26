package ge.luka.melodia.domain.model


/**
 * Contains info about the current playback state of the media player,
 * such as whether the player is playing media or paused, shuffle mode and repeat mode
 */

enum class PlayerState {
    PLAYING, PAUSED, BUFFERING
}