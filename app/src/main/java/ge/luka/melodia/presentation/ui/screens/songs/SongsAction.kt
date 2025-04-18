package ge.luka.melodia.presentation.ui.screens.songs

import ge.luka.melodia.domain.model.SongModel

sealed interface SongsAction {
    data class PlayPressed(val songs: List<SongModel>) : SongsAction
    data class ShufflePressed(val songs: List<SongModel>) : SongsAction
    data object DialogDismiss : SongsAction
    data class MetadataSaved(
        val id: Long,
        val title: String,
        val artist: String,
        val album: String,
        val artworkUri: String?
    ) : SongsAction

    data class SongPressed(val songs: List<SongModel>, val index: Int) : SongsAction
    data class SongLongPressed(val song: SongModel) : SongsAction
}