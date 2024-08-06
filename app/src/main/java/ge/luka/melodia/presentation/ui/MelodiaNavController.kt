@file:OptIn(ExperimentalPermissionsApi::class)

package ge.luka.melodia.presentation.ui

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ge.luka.melodia.presentation.ui.albums.AlbumsScreen
import ge.luka.melodia.presentation.ui.artists.ArtistsScreen
import ge.luka.melodia.presentation.ui.library.LibraryScreen
import ge.luka.melodia.presentation.ui.playlists.PlaylistsScreen
import ge.luka.melodia.presentation.ui.songs.SongsScreen
import ge.luka.melodia.presentation.utils.SinglePermissionRequest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MelodiaNavController(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onUpdateRoute: (String?) -> Unit,
) {
    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.READ_MEDIA_AUDIO)
    } else {
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val startDestinationScreen =
        if (permissionState.status.isGranted) MelodiaScreen.Library else MelodiaScreen.Permission

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
            SongsScreen(navHostController = navController, onUpdateRoute = onUpdateRoute)
        }
        composable<MelodiaScreen.Albums> {
            AlbumsScreen(navHostController = navController, onUpdateRoute = onUpdateRoute)
        }
        composable<MelodiaScreen.Artists> {
            ArtistsScreen(navHostController = navController, onUpdateRoute = onUpdateRoute)
        }
        composable<MelodiaScreen.Playlists> {
            PlaylistsScreen(navHostController = navController, onUpdateRoute = onUpdateRoute)
        }
        composable<MelodiaScreen.Permission> {
            SinglePermissionRequest(
                navHostController = navController,
                onUpdateRoute = onUpdateRoute
            )
        }
    }
}