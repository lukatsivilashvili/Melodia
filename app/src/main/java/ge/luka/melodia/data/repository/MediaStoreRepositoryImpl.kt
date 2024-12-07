package ge.luka.melodia.data.repository

import android.content.Context
import ge.luka.melodia.common.util.toDomain
import ge.luka.melodia.common.util.toEntity
import ge.luka.melodia.data.MediaStoreLoader
import ge.luka.melodia.data.database.dao.AlbumsDao
import ge.luka.melodia.data.database.dao.ArtistsDao
import ge.luka.melodia.data.database.dao.SongsDao
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaStoreRepositoryImpl @Inject constructor(
    private val mediaStoreLoader: MediaStoreLoader,
    private val songsDao: SongsDao,
    private val albumsDao: AlbumsDao,
    private val artistsDao: ArtistsDao,
    private val context: Context
) : MediaStoreRepository {

    override suspend fun getAllSongs(): Flow<List<SongModel>> {
        return songsDao.getAllSongsOrderedByTitle()
            .map { it.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getAllAlbums(): Flow<List<AlbumModel>> {
        return albumsDao.getAllAlbumsOrderedByTitle()
            .map { it.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getAllArtists(): Flow<List<ArtistModel>> {
        return artistsDao.getAllArtistsOrderedByTitle()
            .map { it.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getArtistAlbums(artistId: Long): Flow<List<AlbumModel>> {
        return albumsDao.getAlbumsForArtist(artistId = artistId)
            .map { it.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getAlbumSongs(albumId: Long): Flow<List<SongModel>> {
        return songsDao.getAlbumSongsOrderedByTitle(albumId = albumId)
            .map { it.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun cacheAllSongs() {
        val songs = mediaStoreLoader.getSongsList(context).map { it.toEntity() }
        songsDao.insertAllSongs(songs)
    }

    override suspend fun cacheAllAlbums() {
        val albums = mediaStoreLoader.getAlbumList(context).map { it.toEntity() }
        albumsDao.insertAllAlbums(albums)
    }

    override suspend fun cacheAllArtists() {
        val artists = mediaStoreLoader.getArtistsList(context).map { it.toEntity() }
        artistsDao.insertAllArtists(artists)
    }
}