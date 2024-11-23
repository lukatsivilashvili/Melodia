package ge.luka.melodia.presentation.ui.screens.artists

import ge.luka.melodia.domain.model.ArtistModel

data class ArtistsViewState(
    val artistsList: List<ArtistModel> = listOf()
)
