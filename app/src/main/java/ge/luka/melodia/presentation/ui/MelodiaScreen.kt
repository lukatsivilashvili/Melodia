package ge.luka.melodia.presentation.ui

import kotlinx.serialization.Serializable

sealed class MelodiaScreen {

    @Serializable
    data object Library: MelodiaScreen()

    @Serializable
    object Songs: MelodiaScreen()

    @Serializable
    object Albums: MelodiaScreen()

    @Serializable
    object Artists: MelodiaScreen()

    @Serializable
    object Playlists: MelodiaScreen()
}