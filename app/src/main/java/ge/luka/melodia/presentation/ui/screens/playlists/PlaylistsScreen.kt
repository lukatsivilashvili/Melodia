package ge.luka.melodia.presentation.ui.screens.playlists

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {

    BackHandler {
        navHostController.popBackStack()
    }
}