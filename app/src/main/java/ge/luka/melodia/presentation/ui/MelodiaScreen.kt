package ge.luka.melodia.presentation.ui

import ge.luka.melodia.domain.model.AlbumModel
import kotlinx.serialization.Serializable

sealed class MelodiaScreen {

    @Serializable
    data object Library : MelodiaScreen()

    @Serializable
    data object Songs : MelodiaScreen()

    @Serializable
    data class AlbumSongs(val albumId: Long, val albumModel: AlbumModel) : MelodiaScreen()

    @Serializable
    data class ArtistAlbums(val artistName: String, val artistId: Long) : MelodiaScreen()

    @Serializable
    data object Albums : MelodiaScreen()

    @Serializable
    data object Artists : MelodiaScreen()

    @Serializable
    data object Playlists : MelodiaScreen()

    @Serializable
    data object Settings : MelodiaScreen()

    @Serializable
    data object Permission : MelodiaScreen()
}