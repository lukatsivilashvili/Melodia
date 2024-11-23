package ge.luka.melodia.data.repository

import android.content.Context
import ge.luka.melodia.data.MediaStoreLoader
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.domain.model.ArtistModel
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
    private val artistAlbumsCache = mutableMapOf<String, List<AlbumModel>>()
    private val artistsCache = mutableMapOf<String, List<ArtistModel>>()

    override suspend fun getAllSongs(): Flow<List<SongModel>> = flow {
        val cachedSongs = songsCache[ALL_SONGS]
        if (cachedSongs != null) {
            emit(cachedSongs)
            return@flow
        } else {
            val allSongs = mediaStoreLoader.getSongsList(context = context)
            songsCache[ALL_SONGS] = allSongs
            emit(allSongs)
        }
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
        if (songsCache[ALL_SONGS] == null) {
            songsCache[ALL_SONGS] = mediaStoreLoader.getSongsList(context)
        }
    }

    override suspend fun cacheAllAlbums() {
        if (albumsCache[ALL_ALBUMS] == null) {
            albumsCache[ALL_ALBUMS] = mediaStoreLoader.getAlbumList(context)
        }
    }

    override suspend fun cacheAllArtists() {
        if (artistsCache[ALL_ARTISTS] == null) {
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