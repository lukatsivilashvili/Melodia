package ge.luka.melodia.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ArtistModel(
    val artistId: Long?,
    val title: String?,
    val artUri: String? = null
)
