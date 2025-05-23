@file:OptIn(ExperimentalPermissionsApi::class)

package ge.luka.melodia.presentation.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import ge.luka.melodia.presentation.ui.components.singlepermission.SinglePermissionRequest
import ge.luka.melodia.presentation.ui.screens.MelodiaScreen
import ge.luka.melodia.presentation.ui.screens.albums.AlbumsScreen
import ge.luka.melodia.presentation.ui.screens.albumsongs.AlbumSongsScreen
import ge.luka.melodia.presentation.ui.screens.artists.ArtistsScreen
import ge.luka.melodia.presentation.ui.screens.library.LibraryScreen
import ge.luka.melodia.presentation.ui.screens.playlists.PlaylistsScreen
import ge.luka.melodia.presentation.ui.screens.settings.SettingsScreen
import ge.luka.melodia.presentation.ui.screens.songs.SongsScreen

@Composable
fun MelodiaNavController(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onUpdateRoute: (String?) -> Unit,
) {
    val context = LocalContext.current
    val initialPermissionGranted = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        } else {
            checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    val startDestinationScreen = remember {
        if (initialPermissionGranted) MelodiaScreen.Library else MelodiaScreen.Permission
    }

    NavHost(
        navController = navController,
        startDestination = startDestinationScreen,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(300)
            )
        }
    ) {
        composable<MelodiaScreen.Library> {
            LibraryScreen(
                navHostController = navController,
                onUpdateRoute = onUpdateRoute
            )
        }
        composable<MelodiaScreen.Songs> {
            SongsScreen()
        }
        composable<MelodiaScreen.AlbumSongs> {
            val args = it.toRoute<MelodiaScreen.AlbumSongs>()
            AlbumSongsScreen(
                navHostController = navController,
                onUpdateRoute = onUpdateRoute,
                albumId = args.albumId,
                albumTitle = args.albumTitle,
                albumArtist = args.albumArtist,
                albumArt = args.albumArt,
                albumDuration = args.albumDuration
            )
        }

        composable<MelodiaScreen.Albums> {
            val args = it.toRoute<MelodiaScreen.Albums>()
            AlbumsScreen(
                navHostController = navController,
                onUpdateRoute = onUpdateRoute,
                artistId = args.artistId,
                artistName = args.artistName,
            )
        }
        composable<MelodiaScreen.Artists> {
            ArtistsScreen(navHostController = navController, onUpdateRoute = onUpdateRoute)
        }
        composable<MelodiaScreen.Playlists> {
            PlaylistsScreen(navHostController = navController, onUpdateRoute = onUpdateRoute)
        }
        composable<MelodiaScreen.Settings> {
            SettingsScreen(navHostController = navController, onUpdateRoute = onUpdateRoute)
        }
        composable<MelodiaScreen.Permission> {
            SinglePermissionRequest(
                navHostController = navController,
                onUpdateRoute = onUpdateRoute,
            )
        }
    }
}