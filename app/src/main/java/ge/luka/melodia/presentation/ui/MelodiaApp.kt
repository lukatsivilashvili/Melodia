package ge.luka.melodia.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import ge.luka.melodia.R
import ge.luka.melodia.common.extensions.copy
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.common.utils.Utils
import ge.luka.melodia.presentation.ui.components.shared.BarState
import ge.luka.melodia.presentation.ui.screens.MelodiaScreen
import ge.luka.melodia.presentation.ui.screens.nowplaying.NowPlaying
import ge.luka.melodia.presentation.ui.theme.MelodiaThemeWithViewModel

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MelodiaApp() {
    val navController = rememberNavController()
    var currentRoute by remember { mutableStateOf<String?>(null) }
    var bottomPlayerPadding by remember { mutableStateOf(PaddingValues()) }
    var isBottomPlayerVisible by remember { mutableStateOf(false) }

    // Callback function to update currentRoute
    fun updateCurrentRoute(route: String?) {
        currentRoute = route
    }

    MelodiaThemeWithViewModel {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            scrolledContainerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        title = {
                            Text(
                                modifier = Modifier.basicMarquee(),
                                text = currentRoute ?: stringResource(id = R.string.app_name),
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        },
                        navigationIcon = {
                            AnimatedVisibility(
                                visible = currentRoute != "Melodia"
                                    .getScreenFromRoute() &&
                                        currentRoute != MelodiaScreen.Permission.toString()
                                    .getScreenFromRoute(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Localized description",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        },
                        actions = {
                            AnimatedVisibility(
                                visible = currentRoute == stringResource(id = R.string.app_name)
                                    .getScreenFromRoute(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(onClick = {
                                    navController.navigate(MelodiaScreen.Settings)
                                    updateCurrentRoute(
                                        MelodiaScreen.Settings.toString().getScreenFromRoute()
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = "Localized description",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        },
                    )
                },
                content = { innerPadding ->
                    bottomPlayerPadding = innerPadding.copy(top = 0.dp, bottom = 0.dp)
                    MainContent(
                        innerPadding = if (isBottomPlayerVisible) {
                            innerPadding.copy(bottom = Utils.calculateBottomBarHeight())
                        } else {
                            innerPadding
                        },
                        navController = navController,
                        updateCurrentRoute = ::updateCurrentRoute,
                    )
                }
            )
            NowPlaying(
                onShowBottomPlayer = {
                    isBottomPlayerVisible = true
                },
                bottomPlayerPadding = bottomPlayerPadding
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun AnchoredDraggableState<BarState>.update(
    layoutHeightPx: Int,
    barHeightPx: Int
): Int {
    var offset = 0
    updateAnchors(
        DraggableAnchors {
            offset =
                (-barHeightPx + layoutHeightPx)
            BarState.COLLAPSED at offset.toFloat()
            BarState.EXPANDED at 0.0f
        },
        this.currentValue
    )
    return offset
}