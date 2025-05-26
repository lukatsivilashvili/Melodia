package ge.luka.melodia.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ge.luka.melodia.data.database.dao.AlbumsDao
import ge.luka.melodia.data.database.dao.ArtistsDao
import ge.luka.melodia.data.database.dao.SongsDao
import ge.luka.melodia.data.database.model.AlbumModelEntity
import ge.luka.melodia.data.database.model.ArtistModelEntity
import ge.luka.melodia.data.database.model.SongModelEntity
import ge.luka.melodia.data.database.typeconverters.Converters

@Database(
    entities = [SongModelEntity::class, AlbumModelEntity::class, ArtistModelEntity::class],
    version = 4
)
@TypeConverters(Converters::class)
abstract class MelodiaDatabase: RoomDatabase() {

    abstract val songsDao: SongsDao
    abstract val albumsDao: AlbumsDao
    abstract val artistsDao: ArtistsDao
}