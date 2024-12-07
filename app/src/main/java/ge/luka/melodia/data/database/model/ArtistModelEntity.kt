package ge.luka.melodia.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allArtists")
data class ArtistModelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val artistId: Long?,
    val title: String?,
    val artUri: String?
)
