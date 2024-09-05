package ge.luka.melodia.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import ge.luka.melodia.presentation.ui.screens.settings.SettingsScreenVM
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.darkBlueScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.darkGreenScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.lightBlueScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.lightGreenScheme
import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography
import kotlinx.coroutines.launch

@Composable
fun MelodiaTheme(
    viewModel: SettingsScreenVM = hiltViewModel(),
    content: @Composable () -> Unit,
) {

    var isDarkMode by remember { mutableStateOf(false) }
    var currentTheme by remember { mutableStateOf(AppTheme.GREEN) }

    LaunchedEffect(Unit) {
        launch {
            viewModel.isDarkMode.collect { isDarkMode = it }
        }
        launch {
            viewModel.currentTheme.collect { currentTheme = it }
        }
    }

    val colorScheme = when (currentTheme) {
        AppTheme.BLUE -> if (isDarkMode) darkBlueScheme else lightBlueScheme
        AppTheme.GREEN -> if (isDarkMode) darkGreenScheme else lightGreenScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDarkMode
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MelodiaTypography,
        content = content
    )

}