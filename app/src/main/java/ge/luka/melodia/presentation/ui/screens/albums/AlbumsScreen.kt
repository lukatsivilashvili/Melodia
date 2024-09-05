package ge.luka.melodia.presentation.ui.screens.albums

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.presentation.ui.MelodiaScreen
import ge.luka.melodia.presentation.ui.components.shared.GeneralAlbumListItem
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography
import kotlinx.coroutines.launch

@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    viewModel: AlbumsScreenVM = hiltViewModel(),
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {
    var albumsList by remember { mutableStateOf(listOf<AlbumModel>()) }
    val previousRoute =
        navHostController.previousBackStackEntry?.destination?.route?.getScreenFromRoute()

    LaunchedEffect(Unit) {
        launch { viewModel.albumsList.collect { albumsList = it } }
    }

    BackHandler {
        onUpdateRoute.invoke(previousRoute)
        navHostController.popBackStack()
    }

    AlbumsScreenContent(albumsList = albumsList) {
        navHostController.navigate(MelodiaScreen.Songs(it.second))
        onUpdateRoute.invoke(it.first)

    }
}

@Composable
fun AlbumsScreenContent(
    modifier: Modifier = Modifier,
    albumsList: List<AlbumModel>,
    onClick: (Pair<String, Long>) -> Unit
) {
    if (albumsList.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(albumsList) { albumItem ->
                GeneralAlbumListItem(albumItem = albumItem, modifier = modifier.clickable {
                    onClick.invoke(Pair(albumItem.title ?: "", albumItem.albumId ?: 0))
                })
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = modifier.alpha(0.5F),
                text = "No Albums Found",
                style = MelodiaTypography.titleLarge,
            )
        }
    }
}