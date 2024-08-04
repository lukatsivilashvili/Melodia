package ge.luka.melodia.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ge.luka.melodia.presentation.ui.albums.AlbumsScreen
import ge.luka.melodia.presentation.ui.artists.ArtistsScreen
import ge.luka.melodia.presentation.ui.library.LibraryScreen
import ge.luka.melodia.presentation.ui.playlists.PlaylistsScreen
import ge.luka.melodia.presentation.ui.songs.SongsScreen

@Composable
fun MelodiaNavController(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MelodiaScreen.Library
    ) {
        composable<MelodiaScreen.Library> {
            LibraryScreen()
        }
        composable<MelodiaScreen.Songs> {
            SongsScreen()
        }
        composable<MelodiaScreen.Albums> {
            AlbumsScreen()
        }
        composable<MelodiaScreen.Artists> {
            ArtistsScreen()
        }
        composable<MelodiaScreen.Playlists> {
            PlaylistsScreen()
        }
    }
}