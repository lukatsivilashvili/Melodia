package ge.luka.melodia.presentation.ui

import kotlinx.serialization.Serializable

sealed class MelodiaScreen {

    @Serializable
    data object Library : MelodiaScreen()

    @Serializable
    data object Songs : MelodiaScreen()

    @Serializable
    data class AlbumSongs(
        val albumId: Long,
        val albumTitle: String,
        val albumArtist: String,
        val albumArt: String,
        val albumDuration: String
    ) : MelodiaScreen()

    @Serializable
    data class Albums(val artistName: String? = null, val artistId: Long? = null) : MelodiaScreen()

    @Serializable
    data object Artists : MelodiaScreen()

    @Serializable
    data object Playlists : MelodiaScreen()

    @Serializable
    data object Settings : MelodiaScreen()

    @Serializable
    data object Permission : MelodiaScreen()
}