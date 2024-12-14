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

    @Query("""
    UPDATE allSongs SET 
        title = :title,
        artist = :artist,
        album = :album,
        artUri = :artUri
    WHERE songId = :songId
""")
    suspend fun updateSongById(
        songId: Long,
        title: String,
        artist: String?,
        album: String?,
        artUri: String?,
    ): Int

    @Query("SELECT * FROM allSongs ORDER BY title ASC")
    fun getAllSongsOrderedByTitle(): Flow<List<SongModelEntity>>

    @Query("SELECT * FROM allSongs WHERE albumId = :albumId ORDER BY title ASC")
    fun getAlbumSongsOrderedByTitle(albumId: Long): Flow<List<SongModelEntity>>
}