package ge.luka.melodia.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ge.luka.melodia.data.database.MelodiaDatabase
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
        Room.databaseBuilder(context, MelodiaDatabase::class.java, "data.db").build()

    @Provides
    @Singleton
    fun provideSongsDao(database: MelodiaDatabase): SongsDao = database.songsDao

}