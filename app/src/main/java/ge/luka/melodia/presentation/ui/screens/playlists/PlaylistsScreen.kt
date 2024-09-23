package ge.luka.melodia.presentation.ui.screens.playlists

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.navigation.NavHostController
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography

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
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = modifier.alpha(0.5F),
            text = "Coming Soon",
            style = MelodiaTypography.titleLarge,
        )
    }
}