package ge.luka.melodia.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allAlbums")
data class AlbumModelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val albumId: Long? = null,
    val artistId: Long? = null,
    val title: String? = null,
    val artist: String? = null,
    val songCount: Int? = null,
    val artUri: String? = null,
    val duration: String? = null
)
