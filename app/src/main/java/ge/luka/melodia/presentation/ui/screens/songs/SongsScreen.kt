package ge.luka.melodia.presentation.ui.screens.songs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.domain.model.SongModel
import kotlinx.coroutines.launch

@Composable
fun SongsScreen(
    modifier: Modifier = Modifier,
    viewModel: SongsViewModel = hiltViewModel(),
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {
    var songsList by remember { mutableStateOf(listOf<SongModel>()) }
    val previousRoute =
        navHostController.previousBackStackEntry?.destination?.route?.getScreenFromRoute()

    LaunchedEffect(Unit) {
        launch { viewModel.songsList.collect { songsList = it } }
    }

    BackHandler {
        onUpdateRoute.invoke(previousRoute)
        navHostController.popBackStack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Cyan)
    ) {
    }
}