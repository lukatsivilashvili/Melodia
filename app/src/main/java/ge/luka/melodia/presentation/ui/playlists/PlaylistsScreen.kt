package ge.luka.melodia.presentation.ui.playlists

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import ge.luka.melodia.common.extensions.getScreenFromRoute

@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {
    val previousRoute = navHostController.previousBackStackEntry?.destination?.route?.getScreenFromRoute()
    BackHandler {
        onUpdateRoute.invoke(previousRoute)
        navHostController.popBackStack()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {

    }
}