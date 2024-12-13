package ge.luka.melodia.domain.repository

import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.domain.model.SongModel
import kotlinx.coroutines.flow.Flow

interface MediaStoreRepository {
    suspend fun getAllSongs(): Flow<List<SongModel>>
    suspend fun getAllAlbums(): Flow<List<AlbumModel>>
    suspend fun getAllArtists(): Flow<List<ArtistModel>>
    suspend fun getArtistAlbums(artistId: Long): Flow<List<AlbumModel>>
    suspend fun getAlbumSongs(albumId: Long): Flow<List<SongModel>>

    suspend fun cacheAllSongs()
    suspend fun cacheAllAlbums()
    suspend fun cacheAllArtists()


}