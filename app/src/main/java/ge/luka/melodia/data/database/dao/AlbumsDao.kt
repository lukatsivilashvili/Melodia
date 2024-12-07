package ge.luka.melodia.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ge.luka.melodia.data.database.model.AlbumModelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumsDao {


    @Upsert
    suspend fun insertAllAlbums(albums: List<AlbumModelEntity>)

    @Query("SELECT * FROM allAlbums ORDER BY title ASC")
    fun getAllAlbumsOrderedByTitle(): Flow<List<AlbumModelEntity>>

    @Query("SELECT * FROM allAlbums WHERE artistId = :artistId ORDER BY title ASC")
    fun getAlbumsForArtist(artistId: Long): Flow<List<AlbumModelEntity>>
}