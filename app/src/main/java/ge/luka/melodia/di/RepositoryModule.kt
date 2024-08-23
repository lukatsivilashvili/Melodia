package ge.luka.melodia.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.luka.melodia.data.repository.DataStoreRepositoryImpl
import ge.luka.melodia.domain.repository.DataStoreRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideDataStoreRepository(context: Context): DataStoreRepository =
        DataStoreRepositoryImpl(context = context)
}