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

    @Query("SELECT albumId FROM allAlbums WHERE title = :albumName LIMIT 1")
    suspend fun getAlbumIdByName(albumName: String): Long?

    @Query("SELECT EXISTS(SELECT 1 FROM allAlbums WHERE title = :albumName)")
    suspend fun doesAlbumExist(albumName: String): Boolean
}