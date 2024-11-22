package ge.luka.melodia.domain.model

data class ArtistModel(
    val id: Long? = null,
    val title: String?,
    val albums: List<AlbumModel>
)
