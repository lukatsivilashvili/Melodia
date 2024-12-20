package ge.luka.melodia.presentation.ui.screens.songs

import ge.luka.melodia.domain.model.SongModel

sealed interface SongsAction {
    data object PlayPressed : SongsAction
    data object ShufflePressed : SongsAction
    data object DialogDismiss : SongsAction
    data class MetadataSaved(
        val id: Long,
        val title: String,
        val artist: String,
        val album: String,
        val artworkUri: String?
    ) : SongsAction

    data class SongPressed(val song: SongModel) : SongsAction
    data class SongLongPressed(val song: SongModel) : SongsAction
}