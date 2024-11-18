package ge.luka.melodia.presentation.ui.screens.artists

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.domain.model.ArtistModel
import ge.luka.melodia.presentation.ui.MelodiaScreen
import ge.luka.melodia.presentation.ui.components.shared.GeneralArtistListItem
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography
import kotlinx.coroutines.launch

@Composable
fun ArtistsScreen(
    modifier: Modifier = Modifier, navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {
    navHostController.previousBackStackEntry?.destination?.route?.getScreenFromRoute()

    LaunchedEffect(Unit) {
        onUpdateRoute.invoke("Artists")
    }

    BackHandler {
        navHostController.popBackStack()
    }

    ArtistsScreenContent {
        navHostController.navigate(MelodiaScreen.ArtistAlbums(it.first, it.second))
        onUpdateRoute.invoke(it.first)
    }
}

@Composable
fun ArtistsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: ArtistsScreenVM = hiltViewModel(),
    onClick: (Pair<String, Long>) -> Unit
) {
    var artistsList by remember { mutableStateOf(listOf<ArtistModel>()) }

    LaunchedEffect(Unit) {
        launch { viewModel.artistsList.collect { artistsList = it } }
    }

    val derivedArtistsList by remember {
        derivedStateOf { artistsList }
    }

    if (derivedArtistsList.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize(), state = rememberLazyListState()) {
            items(items = derivedArtistsList, key = { it.title ?: 0 }) { artistItem ->
                GeneralArtistListItem(modifier = modifier.clickable {
                    onClick.invoke(
                        Pair(
                            first = artistItem.title ?: "",
                            second = artistItem.id ?: 0
                        )
                    )
                }, artistItem = artistItem)
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = modifier.alpha(0.5F),
                text = "No Artists Found",
                style = MelodiaTypography.titleLarge,
            )
        }
    }
}
