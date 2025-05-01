package ge.luka.melodia.presentation.ui.screens.artists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ge.luka.melodia.common.mvi.CollectSideEffects
import ge.luka.melodia.presentation.ui.components.shared.GeneralArtistListItem
import ge.luka.melodia.presentation.ui.screens.MelodiaScreen
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography

@Composable
fun ArtistsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {
    ArtistsScreenContent(
        modifier = modifier,
        navHostController = navHostController,
        onUpdateRoute = onUpdateRoute
    )
}

@Composable
fun ArtistsScreenContent(
    modifier: Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit,
    viewModel: ArtistsScreenVM = hiltViewModel()
) {
    val viewState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectSideEffects(flow = viewModel.sideEffect) { effect ->
        when (effect) {
            is ArtistsSideEffect.NavigateToAlbums -> {
                navHostController.navigate(
                    MelodiaScreen.Albums(
                        artistName = effect.artistName,
                        artistId = effect.artistId
                    )
                )
                onUpdateRoute.invoke(effect.artistName)
            }
        }
    }

    if (viewState.artistsList.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize(), state = rememberLazyListState()) {
            items(items = viewState.artistsList, key = { it.title ?: 0 }) { artistItem ->
                GeneralArtistListItem(modifier = modifier.clickable {
                    viewModel.onAction(
                        ArtistsAction.OnArtistClicked(
                            artistId = artistItem.artistId ?: 0,
                            artistName = artistItem.title ?: ""
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
