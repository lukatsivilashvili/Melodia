package ge.luka.melodia

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ge.luka.melodia.data.repository.DataStoreRepositoryImpl
import ge.luka.melodia.domain.repository.DataStoreRepository

@HiltAndroidApp
class MelodiaApplication: Application() {
    private lateinit var dataStoreRepository: DataStoreRepository

    override fun onCreate() {
        super.onCreate()
        dataStoreRepository = DataStoreRepositoryImpl(context = this)
    }
}