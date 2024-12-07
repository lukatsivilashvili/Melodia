package ge.luka.melodia.data.repository

import android.content.Context
import android.util.Log
import android.util.Log.d
import ge.luka.melodia.common.util.toDomain
import ge.luka.melodia.common.util.toEntity
import ge.luka.melodia.data.MediaStoreLoader
import ge.luka.melodia.data.database.dao.SongsDao
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.domain.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MediaStoreRepositoryImpl @Inject constructor(
    private val mediaStoreLoader: MediaStoreLoader,
    private val songsDao: SongsDao,
    private val context: Context
) : MediaStoreRepository {

    private val albumsCache = mutableMapOf<String, List<AlbumModel>>()
    private val artistAlbumsCache = mutableMapOf<String, List<AlbumModel>>()
    private val artistsCache = mutableMapOf<String, List<ArtistModel>>()

    override suspend fun getAllSongs(): Flow<List<SongModel>> {
        return songsDao.getAllSongsOrderedByTitle()
            .onEach { entities ->
                Log.d("MediaStoreRepo", "Raw entities from DB: ${entities.size}")
            }
            .map { it.toDomain() }
            .onEach { models ->
                Log.d("MediaStoreRepo", "Transformed models: ${models.size}")
            }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getAllAlbums(): Flow<List<AlbumModel>> = flow {
        val cachedAlbums = albumsCache[ALL_ALBUMS]
        if (cachedAlbums != null) {
            emit(cachedAlbums)
            return@flow
        } else {
            val allAlbums = mediaStoreLoader.getAlbumList(context = context)
            albumsCache[ALL_ALBUMS] = allAlbums
            emit(allAlbums)
        }
    }

    override suspend fun getArtistAlbums(artistId: Long): Flow<List<AlbumModel>> = flow {
        val cachedArtistAlbums = artistAlbumsCache["$artistId"]
        if (cachedArtistAlbums != null) {
            emit(cachedArtistAlbums)
            return@flow
        } else {
            val artistAlbums =
                mediaStoreLoader.getArtistAlbumList(context = context, artistId = artistId)
            artistAlbumsCache["$artistId"] = artistAlbums
            emit(artistAlbums)
        }
    }

    override suspend fun cacheAllSongs() {
        // Only insert new songs, don't clear existing ones
        val songs = mediaStoreLoader.getSongsList(context).map { it.toEntity() }
        d("allSongs", songs.toString())
        songsDao.insertAllSongs(songs) // Using @Upsert will handle duplicates
    }

    override suspend fun cacheAllAlbums() {
        if (albumsCache[ALL_ALBUMS].isNullOrEmpty()) {
            albumsCache[ALL_ALBUMS] = mediaStoreLoader.getAlbumList(context)
        }
    }

    override suspend fun cacheAllArtists() {
        if (artistsCache[ALL_ARTISTS].isNullOrEmpty()) {
            artistsCache[ALL_ARTISTS] = mediaStoreLoader.getArtistsList(context)
        }
    }

    override suspend fun getAllArtists(): Flow<List<ArtistModel>> = flow {
        val cachedArtists = artistsCache[ALL_ARTISTS]
        if (cachedArtists != null) {
            emit(cachedArtists)
            return@flow
        } else {
            val allArtists = mediaStoreLoader.getArtistsList(context = context)
            artistsCache[ALL_ARTISTS] = allArtists
            emit(allArtists)
        }
    }

    companion object {
        const val ALL_SONGS = "allSongs"
        const val ALL_ALBUMS = "allAlbums"
        const val ALL_ARTISTS = "allArtists"
    }
}