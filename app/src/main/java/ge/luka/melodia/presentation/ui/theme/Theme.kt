package ge.luka.melodia.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.darkBlueScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.darkGreenScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.lightBlueScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.lightGreenScheme
import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography
import kotlinx.coroutines.launch

@Composable
fun MelodiaTheme(
    viewModel: ThemeVM = hiltViewModel(),
    content: @Composable () -> Unit,
) {

    val viewState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        launch {
            viewModel.isDarkMode.collect {
                viewModel.onAction(
                    ThemeAction.DarkModeReceived(
                        isDarkMode = it
                    )
                )
            }
        }
        launch {
            viewModel.currentTheme.collect {
                viewModel.onAction(
                    ThemeAction.ThemeColorReceived(
                        newTheme = it
                    )
                )
            }
        }
    }

    val colorScheme = when (viewState.currentTheme) {
        AppTheme.BLUE -> if (viewState.isDarkMode) darkBlueScheme else lightBlueScheme
        AppTheme.GREEN -> if (viewState.isDarkMode) darkGreenScheme else lightGreenScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = viewState.isDarkMode
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MelodiaTypography,
        content = content
    )

}