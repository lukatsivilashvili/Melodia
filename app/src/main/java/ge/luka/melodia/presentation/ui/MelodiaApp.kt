package ge.luka.melodia.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import ge.luka.melodia.R
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.RepeatMode
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.components.MelodiaNavController
import ge.luka.melodia.presentation.ui.components.bottomplayer.BottomPlayer
import ge.luka.melodia.presentation.ui.components.bottomplayer.NowPlayingState
import ge.luka.melodia.presentation.ui.theme.MelodiaThemeWithViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MelodiaApp() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var currentRoute by remember { mutableStateOf<String?>(null) }

    // Callback function to update currentRoute
    fun updateCurrentRoute(route: String?) {
        currentRoute = route

        scrollBehavior.state.heightOffset = 0f
        scrollBehavior.state.contentOffset = 0f
    }

    MelodiaThemeWithViewModel {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MediumTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ), title = {
                    Text(
                        modifier = Modifier.basicMarquee(),
                        text = currentRoute ?: stringResource(id = R.string.app_name),
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }, navigationIcon = {
                    AnimatedVisibility(
                        visible = currentRoute != MelodiaScreen.Library.toString()
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

                }, actions = {
                    AnimatedVisibility(
                        visible = currentRoute == MelodiaScreen.Library.toString()
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
                }, scrollBehavior = scrollBehavior

                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        MelodiaNavController(
                            modifier = Modifier
                                .fillMaxSize(),
                            navController = navController,
                            onUpdateRoute = ::updateCurrentRoute,
                        )
                    }

                    BottomPlayer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        nowPlayingState = NowPlayingState.Playing(
                            song = SongModel(
                                songId = 1L,
                                albumId = 101L,
                                artistId = 201L,
                                title = "Song One",
                                artist = "Artist One",
                                album = "Album One",
                                artUri = "",
                                duration = 210000L,
                                songPath = "/music/song_one.mp3",
                                bitrate = 320
                            ),
                            playbackState = PlayerState.PAUSED,
                            repeatMode = RepeatMode.NO_REPEAT,
                            isShuffleOn = false
                        ),
                        songProgressProvider = { 1f },
                        enabled = true,
                        onTogglePlayback = {},
                        onNext = {},
                        onPrevious = {}
                    )
                }
            }
        )
    }
}