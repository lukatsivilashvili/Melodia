package ge.luka.melodia.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ge.luka.melodia.data.database.model.ArtistModelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistsDao {

    @Upsert
    suspend fun insertAllArtists(artists: List<ArtistModelEntity>)

    @Upsert
    suspend fun insertSingleArtist(artist: ArtistModelEntity)

    @Query("SELECT * FROM allArtists ORDER BY title ASC")
    fun getAllArtistsOrderedByTitle(): Flow<List<ArtistModelEntity>>

    @Query("SELECT artistId FROM allArtists WHERE title = :artistName LIMIT 1")
    suspend fun getArtistIdByName(artistName: String): Long?

    @Query("""
    UPDATE allArtists SET 
        artUri = :artUri
    WHERE artistId = :artistId
""")
    suspend fun updateArtistArt(
        artistId: Long,
        artUri: String?,
    ): Int
}