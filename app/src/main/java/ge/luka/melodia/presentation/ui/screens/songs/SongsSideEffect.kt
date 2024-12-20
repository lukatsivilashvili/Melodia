package ge.luka.melodia.presentation.ui.screens.songs

sealed interface SongsSideEffect {
    data class ThrowToast(val message: String) : SongsSideEffect
    data class UpdateCurrentSong(val id: Long, val title: String, val artist: String, val album: String, val artworkUri: String?) : SongsSideEffect
}