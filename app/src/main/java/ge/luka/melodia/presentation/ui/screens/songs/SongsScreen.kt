package ge.luka.melodia.presentation.ui.screens.songs

import androidx.activity.compose.BackHandler
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
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.components.shared.GeneralMusicListItem
import ge.luka.melodia.presentation.ui.components.shared.HelperControlButtons
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography

@Composable
fun SongsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {
    LaunchedEffect(Unit) {
        onUpdateRoute.invoke("Songs")
    }

    BackHandler {
        navHostController.popBackStack()
    }

    SongsScreenContent()
}

@Composable
fun SongsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: SongsScreenVM = hiltViewModel(),
) {
    var songsList by remember { mutableStateOf(listOf<SongModel>()) }

    LaunchedEffect(Unit) {
        viewModel.songsList.collect {
            songsList = it
        }
    }

    val derivedSongsList by remember {
        derivedStateOf { songsList }
    }

    // Use derivedSongsList in your LazyColumn
    if (derivedSongsList.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize(), state = rememberLazyListState()) {
            item { HelperControlButtons() }
            items(items = derivedSongsList, key = { it.songId ?: 0 }) { songItem ->
                GeneralMusicListItem(songItem = songItem)
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = modifier.alpha(0.5F),
                text = "No Songs Found",
                style = MelodiaTypography.titleLarge,
            )
        }
    }
}