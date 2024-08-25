package ge.luka.melodia.domain.repository

import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.SongModel
import kotlinx.coroutines.flow.Flow

interface MediaStoreRepository {
    suspend fun getAllSongs(): Flow<List<SongModel>>
    suspend fun getAllAlbums(): Flow<List<AlbumModel>>
}