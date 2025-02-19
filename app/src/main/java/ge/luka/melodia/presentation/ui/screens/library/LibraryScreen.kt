package ge.luka.melodia.presentation.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ge.luka.melodia.R
import ge.luka.melodia.common.mvi.CollectSideEffects
import ge.luka.melodia.presentation.ui.components.shared.LibraryListItem
import ge.luka.melodia.presentation.ui.theme.MelodiaTheme

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController?,
    onUpdateRoute: ((String?) -> Unit)?
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
    navHostController: NavHostController? = null,
    onUpdateRoute: ((String?) -> Unit)? = null
) {
    CollectSideEffects(flow = viewModel.sideEffect) { effect ->
        when (effect) {
            is LibrarySideEffect.NavigateToAlbums -> navHostController?.navigate(effect.screen)
            is LibrarySideEffect.NavigateToArtists -> navHostController?.navigate(effect.screen)
            is LibrarySideEffect.NavigateToLibrary -> navHostController?.navigate(effect.screen)
            is LibrarySideEffect.NavigateToPlaylists -> navHostController?.navigate(effect.screen)
            is LibrarySideEffect.NavigateToSongs -> navHostController?.navigate(effect.screen)
        }
    }

    LaunchedEffect(Unit) {
        onUpdateRoute?.invoke("Library")
    }

    val navigateToScreen: (String) -> Unit = { screen ->
        viewModel.onAction(LibraryAction.LibraryItemClicked(libraryItem = screen))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LibraryListItem(
            title = "Songs",
            icon = R.drawable.ic_songs,
            navigateToScreen = navigateToScreen
        )
        LibraryListItem(
            title = "Albums",
            icon = R.drawable.ic_albums,
            navigateToScreen = navigateToScreen
        )
        LibraryListItem(
            title = "Artists",
            icon = R.drawable.ic_artist,
            navigateToScreen = navigateToScreen
        )
        LibraryListItem(
            title = "Playlists",
            icon = R.drawable.ic_playlists,
            navigateToScreen = navigateToScreen
        )
    }
}

@Preview
@Composable
fun LibraryScreenPreview(modifier: Modifier = Modifier) {
    MelodiaTheme {
        LibraryScreenContent(modifier = modifier)
    }
}