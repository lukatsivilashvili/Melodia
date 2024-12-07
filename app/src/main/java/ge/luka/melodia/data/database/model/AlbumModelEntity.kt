package ge.luka.melodia.data.database.model

data class AlbumModelEntity(
    val albumId: Long? = null,
    val title: String? = null,
    val artist: String? = null,
    val songCount: Int? = null,
    val artUri: String? = null,
    val duration: String? = null
)
