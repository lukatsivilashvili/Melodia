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
import ge.luka.melodia.domain.model.AlbumModel
import ge.luka.melodia.presentation.ui.MelodiaScreen
import ge.luka.melodia.presentation.ui.components.shared.GeneralAlbumListItem
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography
import kotlinx.coroutines.launch

@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {
    val previousRoute =
        navHostController.previousBackStackEntry?.destination?.route?.getScreenFromRoute()



    BackHandler {
        onUpdateRoute.invoke(previousRoute)
        navHostController.popBackStack()
    }

    AlbumsScreenContent {
        navHostController.navigate(MelodiaScreen.AlbumSongs(it.second, it.third))
        onUpdateRoute.invoke(it.first)

    }
}

@Composable
fun AlbumsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: AlbumsScreenVM = hiltViewModel(),
    onClick: (Triple<String, Long, AlbumModel>) -> Unit
) {
    var albumsList by remember { mutableStateOf(listOf<AlbumModel>()) }

    LaunchedEffect(Unit) {
        launch { viewModel.albumsList.collect { albumsList = it } }
    }

    val derivedSongsList by remember {
        derivedStateOf { albumsList }
    }

    if (derivedSongsList.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(derivedSongsList) { albumItem ->
                GeneralAlbumListItem(albumItem = albumItem, modifier = modifier.clickable {
                    onClick.invoke(
                        Triple(
                            first = albumItem.title ?: "",
                            second = albumItem.albumId ?: 0,
                            third = albumItem
                        )
                    )
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