package ge.luka.melodia.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ge.luka.melodia.data.database.dao.SongsDao
import ge.luka.melodia.data.database.model.SongModelEntity

@Database(
    entities = [SongModelEntity::class],
    version = 1
)
abstract class MelodiaDatabase: RoomDatabase() {

    abstract val songsDao: SongsDao
}