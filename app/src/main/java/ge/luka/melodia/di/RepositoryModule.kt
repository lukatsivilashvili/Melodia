package ge.luka.melodia.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.luka.melodia.data.MediaStoreLoader
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
            mediaStoreLoader: MediaStoreLoader,
            songsDao: SongsDao,
            context: Context
        ): MediaStoreRepository {
            return MediaStoreRepositoryImpl(mediaStoreLoader = mediaStoreLoader, context = context, songsDao = songsDao)
        }
    }
}