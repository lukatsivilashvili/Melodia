package ge.luka.melodia.domain.model

interface BaseModel {
    val songId: Long?
    val albumId: Long?
    val artistId: Long?
    val title: String?
    val artist: String?
    val album: String?
    val artUri: String?
}