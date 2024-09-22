package ge.luka.melodia.presentation.ui.screens.artists

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
import ge.luka.melodia.domain.model.ArtistModel
import kotlinx.coroutines.launch

@Composable
fun ArtistsScreen(
    modifier: Modifier = Modifier, navHostController: NavHostController,
    viewModel: ArtistsScreenVM = hiltViewModel(),
    onUpdateRoute: (String?) -> Unit
) {
    var artistsList by remember { mutableStateOf(listOf<ArtistModel>()) }
    val previousRoute =
        navHostController.previousBackStackEntry?.destination?.route?.getScreenFromRoute()

    LaunchedEffect(Unit) {
        launch { viewModel.artistsList.collect { artistsList = it } }
    }

    BackHandler {
        onUpdateRoute.invoke(previousRoute)
        navHostController.popBackStack()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {

        println(artistsList)

    }
}