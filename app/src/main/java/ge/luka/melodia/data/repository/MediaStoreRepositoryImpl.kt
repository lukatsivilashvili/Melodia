package ge.luka.melodia.data.repository

import android.content.Context
import ge.luka.melodia.common.transformers.toDomain
import ge.luka.melodia.common.transformers.toEntity
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

    override suspend fun cacheAllSongs(songModel: SongModel) {
        val song = songModel.toEntity()
        songsDao.insertSingleSong(song)
    }

    override suspend fun cacheAllAlbums() {
        val albums = mediaStoreLoader.getAlbumList(context).map { it.toEntity() }
        albumsDao.insertAllAlbums(albums)
    }

    override suspend fun cacheAllArtists() {
        val artists = mediaStoreLoader.getArtistsList(context).map { it.toEntity() }
        artistsDao.insertAllArtists(artists)
    }

    override suspend fun updateSongRecord(
        songId: Long,
        title: String,
        artist: String?,
        album: String?,
        artUri: String?,
    ): Boolean {
        if (album != null && !albumsDao.doesAlbumExist(album)) {
            return false // Abort the update since the album doesn't exist
        }

        val albumId = album?.let { albumsDao.getAlbumIdByName(it) }
        val artistId = artist?.let { artistsDao.getArtistIdByName(it) }

        songsDao.updateSongById(
            songId = songId,
            title = title,
            artist = artist,
            album = album,
            artUri = artUri
        )

        songsDao.updateSongIdsById(songId = songId, albumId = albumId, artistId = artistId)

        return true
    }

    override suspend fun updateAlbumRecord(
        artistId: Long,
        albumId: Long,
        title: String,
        artist: String?,
        artUri: String?
    ): Boolean {
        albumsDao.updateAlbumById(
            albumId = albumId,
            title = title,
            artist = artist,
            artUri = artUri
        )
        artistsDao.updateArtistArt(artistId, artUri)
        songsDao.updateSongArtByAlbumId(albumId, artUri)
        return true
    }
}