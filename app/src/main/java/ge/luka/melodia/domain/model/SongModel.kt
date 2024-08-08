package ge.luka.melodia.domain.model

data class SongModel(
    val songId: Long? = null,
    val title: String? = null,
    val artist: String? = null,
    val album: String? = null,
    val albumId: Long? = null,
    val duration: Long? = null,
    val path: String? = null,
    val artUri: String? = null
)
