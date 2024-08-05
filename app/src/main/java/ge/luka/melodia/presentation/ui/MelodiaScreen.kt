package ge.luka.melodia.presentation.ui

import kotlinx.serialization.Serializable

sealed class MelodiaScreen {

    @Serializable
    data object Library: MelodiaScreen()

    @Serializable
    data object Songs: MelodiaScreen()

    @Serializable
    data object Albums: MelodiaScreen()

    @Serializable
    data object Artists: MelodiaScreen()

    @Serializable
    data object Playlists: MelodiaScreen()

    @Serializable
    data object Settings: MelodiaScreen()

    @Serializable
    data object Permission: MelodiaScreen()
}