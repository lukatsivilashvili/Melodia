package ge.luka.melodia.presentation.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ge.luka.melodia.R
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.presentation.ui.MelodiaScreen
import ge.luka.melodia.presentation.ui.components.shared.LibraryListItem
import ge.luka.melodia.presentation.ui.theme.MelodiaTheme

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    viewModel: LibraryScreenViewModel? = hiltViewModel(),
    navHostController: NavHostController?,
    onUpdateRoute: ((String?) -> Unit)?
) {
    LibraryScreenContent(
        modifier = modifier,
        viewModel = viewModel,
        navHostController = navHostController,
        onUpdateRoute = onUpdateRoute
    )
}

@Composable
fun LibraryScreenContent(
    modifier: Modifier,
    viewModel: LibraryScreenViewModel? = null,
    navHostController: NavHostController? = null,
    onUpdateRoute: ((String?) -> Unit)? = null
) {
    val navigateToScreen: (String) -> Unit = { screen ->
        val destination = viewModel?.setDestinationScreen(screen = screen)
        navHostController?.navigate(destination ?: MelodiaScreen.Library)
        onUpdateRoute?.invoke(destination.toString().getScreenFromRoute())

    }
    Column(
        modifier = modifier
            .fillMaxSize()
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
            icon = R.drawable.ic_artists,
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