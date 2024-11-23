package ge.luka.melodia.presentation.ui.screens.songs

import ge.luka.melodia.domain.model.SongModel

data class SongsViewState(
    val songsList: List<SongModel> = listOf(),
)