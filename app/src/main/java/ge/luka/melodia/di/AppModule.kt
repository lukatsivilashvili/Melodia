package ge.luka.melodia.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ge.luka.melodia.data.MediaStoreLoader
import ge.luka.melodia.data.repository.MediaStoreRepositoryImpl
import ge.luka.melodia.domain.repository.MediaStoreRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideMusicDatabase() = MediaStoreLoader

    @Provides
    @Singleton
    fun addMediaStoreRepo(
        mediaStoreLoader: MediaStoreLoader,
        context: Context
    ): MediaStoreRepository =
        MediaStoreRepositoryImpl(mediaStoreLoader = mediaStoreLoader, context = context)
}