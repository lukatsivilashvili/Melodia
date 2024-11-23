package ge.luka.melodia.presentation.ui.screens.albumsongs

import ge.luka.melodia.domain.model.SongModel

data class AlbumSongsViewState(
    val songsList: List<SongModel> = listOf(),
)