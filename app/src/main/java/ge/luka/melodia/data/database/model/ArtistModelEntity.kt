package ge.luka.melodia.data.database.model

import ge.luka.melodia.domain.model.AlbumModel

data class ArtistModelEntity(
    val id: Long? = null,
    val title: String?,
    val albums: List<AlbumModel>
)
