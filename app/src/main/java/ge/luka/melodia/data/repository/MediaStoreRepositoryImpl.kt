package ge.luka.melodia.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import ge.luka.melodia.data.MediaStoreLoader
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
class MediaStoreRepositoryImpl @Inject constructor(
    private val mediaStoreLoader: MediaStoreLoader,
    private val context: Context
) : MediaStoreRepository {
    override suspend fun getAllSongs(): Flow<List<SongModel>> = flow {
        emit(mediaStoreLoader.getSongsList(context = context))
    }

    override suspend fun getAllAlbums(): Flow<List<AlbumModel>> = flow {
        emit(mediaStoreLoader.getAlbumList(context = context))

    }
}