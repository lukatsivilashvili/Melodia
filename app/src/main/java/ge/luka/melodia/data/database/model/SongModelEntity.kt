package ge.luka.melodia.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allSongs")
data class SongModelEntity(
    @PrimaryKey(autoGenerate = false)
    val songId: Long? = null,
    val albumId: Long? = null,
    val artistId: Long? = null,
    val title: String? = null,
    val album: String? = null,
    val artist: String? = null,
    val trackNumber: Int? = null,
    val duration: Long? = null,
    val songPath: String? = null,
    val artUri: String? = null,
    val bitrate: Int? = null,
    val palette: Map<String, String>? = null
)

