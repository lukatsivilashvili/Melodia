package ge.luka.melodia.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ge.luka.melodia.data.database.MelodiaDatabase
import ge.luka.melodia.data.database.dao.AlbumsDao
import ge.luka.melodia.data.database.dao.ArtistsDao
import ge.luka.melodia.data.database.dao.SongsDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MelodiaDatabase =
        Room.databaseBuilder(context, MelodiaDatabase::class.java, "data.db").fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideSongsDao(database: MelodiaDatabase): SongsDao = database.songsDao

    @Provides
    @Singleton
    fun provideAlbumsDao(database: MelodiaDatabase): AlbumsDao = database.albumsDao

    @Provides
    @Singleton
    fun provideArtistsDao(database: MelodiaDatabase): ArtistsDao = database.artistsDao

}