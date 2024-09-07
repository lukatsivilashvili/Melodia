package ge.luka.melodia.data.repository

import android.content.Context
import ge.luka.melodia.data.MediaStoreLoader
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MediaStoreRepositoryImpl @Inject constructor(
    private val mediaStoreLoader: MediaStoreLoader,
    private val context: Context
) : MediaStoreRepository {

    private val songsCache = mutableMapOf<String, List<SongModel>>()
    private val albumsCache = mutableMapOf<String, List<AlbumModel>>()

    override suspend fun getAllSongs(): Flow<List<SongModel>> = flow {
        val cachedSongs = songsCache["allSongs"]
        if (cachedSongs != null) {
            emit(cachedSongs)
            return@flow
        } else {
            val allSongs = mediaStoreLoader.getSongsList(context = context)
            songsCache["allSongs"] = allSongs
            emit(allSongs)
        }
    }

    override suspend fun getAllAlbums(): Flow<List<AlbumModel>> = flow {
        val cachedAlbums = albumsCache["allAlbums"]
        if (cachedAlbums != null) {
            emit(cachedAlbums)
            return@flow
        } else {
            val allAlbums = mediaStoreLoader.getAlbumList(context = context)
            albumsCache["allAlbums"] = allAlbums
            emit(allAlbums)
        }
    }
}