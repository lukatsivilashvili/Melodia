package ge.luka.melodia.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.rememberNavController
import ge.luka.melodia.R
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.presentation.ui.components.MelodiaNavController
import ge.luka.melodia.presentation.ui.theme.MelodiaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MelodiaApp() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var currentRoute by remember { mutableStateOf<String?>(null) }
    navController.previousBackStackEntry?.destination?.route?.getScreenFromRoute()

    // Callback function to update currentRoute
    fun updateCurrentRoute(route: String?) {
        currentRoute = route
    }

    MelodiaTheme{
        LaunchedEffect(navController) {
            snapshotFlow { navController.currentBackStackEntry?.destination?.route }.collect { route ->
                currentRoute = route?.getScreenFromRoute()
            }
        }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MediumTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ), title = {
                    Text(
                        currentRoute ?: stringResource(id = R.string.app_name),
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
                            val previousRoute =
                                navController.previousBackStackEntry?.destination?.route?.getScreenFromRoute()
                            navController.popBackStack()
                            updateCurrentRoute(previousRoute) // Update after popBackStack
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
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    MelodiaNavController(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        navController = navController,
                        onUpdateRoute = ::updateCurrentRoute,
                    )
                }
            }
        )
    }
}