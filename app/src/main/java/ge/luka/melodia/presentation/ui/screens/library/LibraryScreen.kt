package ge.luka.melodia.presentation.ui.screens.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ge.luka.melodia.presentation.ui.screens.albums.AlbumsScreen
import ge.luka.melodia.presentation.ui.screens.artists.ArtistsScreen
import ge.luka.melodia.presentation.ui.screens.playlists.PlaylistsScreen
import ge.luka.melodia.presentation.ui.screens.songs.SongsScreen
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: ((String?) -> Unit)
) {
    LibraryScreenContent(
        modifier = modifier,
        navHostController = navHostController,
        onUpdateRoute = onUpdateRoute
    )
}

@Composable
fun LibraryScreenContent(
    modifier: Modifier,
    viewModel: LibraryScreenVM = hiltViewModel(),
    navHostController: NavHostController,
    onUpdateRoute: ((String?) -> Unit)
) {
    val tabScreens = listOf(
        TabScreen("Songs") { SongsScreen() },
        TabScreen("Albums") {
            AlbumsScreen(
                navHostController = navHostController,
                onUpdateRoute = onUpdateRoute
            )
        },
        TabScreen("Artists") {
            ArtistsScreen(
                navHostController = navHostController,
                onUpdateRoute = onUpdateRoute
            )
        },
        TabScreen("Playlists") {
            PlaylistsScreen(
                navHostController = navHostController,
                onUpdateRoute = onUpdateRoute
            )
        }
    )

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { tabScreens.size })
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    LaunchedEffect(Unit) {
        onUpdateRoute.invoke("Melodia")
    }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex.value,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabScreens.forEachIndexed { index, screen ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(text = screen.title) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                tabScreens[index].screen()
            }
        }
    }
}