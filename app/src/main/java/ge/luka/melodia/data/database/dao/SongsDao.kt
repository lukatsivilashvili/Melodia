package ge.luka.melodia.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ge.luka.melodia.data.database.model.SongModelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongsDao {

    @Upsert
    suspend fun insertAllSongs(songs: List<SongModelEntity>)

    @Query("SELECT * FROM allSongs ORDER BY title ASC")
    fun getAllSongsOrderedByTitle(): Flow<List<SongModelEntity>>

    @Query("SELECT * FROM allSongs WHERE albumId = :albumId ORDER BY title ASC")
    fun getAlbumSongsOrderedByTitle(albumId: Long): Flow<List<SongModelEntity>>
}