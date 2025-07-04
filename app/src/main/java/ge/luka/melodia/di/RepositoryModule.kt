package ge.luka.melodia.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.luka.melodia.data.database.dao.AlbumsDao
import ge.luka.melodia.data.database.dao.ArtistsDao
import ge.luka.melodia.data.database.dao.SongsDao
import ge.luka.melodia.data.repository.MediaStoreRepositoryImpl
import ge.luka.melodia.data.repository.ThemeRepositoryImpl
import ge.luka.melodia.domain.repository.MediaStoreRepository
import ge.luka.melodia.domain.repository.PermissionRepository
import ge.luka.melodia.domain.repository.ThemeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPermissionRepository(repositoryImpl: PermissionRepository): PermissionRepository

    @Binds
    abstract fun bindThemeRepository(repositoryImpl: ThemeRepositoryImpl): ThemeRepository

    companion object {
        @Provides
        @Singleton
        fun provideMediaStoreRepository(
            songsDao: SongsDao,
            albumsDao: AlbumsDao,
            artistsDao: ArtistsDao,
        ): MediaStoreRepository {
            return MediaStoreRepositoryImpl(
                songsDao = songsDao,
                albumsDao = albumsDao,
                artistsDao = artistsDao,
            )
        }
    }
}