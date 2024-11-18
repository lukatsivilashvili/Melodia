package ge.luka.melodia.presentation.ui.screens.playlists

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.navigation.NavHostController
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography

@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {
    LaunchedEffect(Unit) {
        onUpdateRoute.invoke("Playlists")
    }

    BackHandler {
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