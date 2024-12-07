package ge.luka.melodia.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allSongs")
data class SongModelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val songId: Long? = null,
    val albumId: Long? = null,
    val title: String? = null,
    val album: String? = null,
    val artist: String? = null,
    val duration: Long? = null,
    val songPath: String? = null,
    val artUri: String? = null,
    val bitrate: Int? = null
)

