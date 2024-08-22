package ge.luka.melodia.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.darkBlueScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.darkGreenScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.lightBlueScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.lightGreenScheme

@Composable
fun MelodiaTheme(
    viewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit,
) {

    val isDark = viewModel.isDarkMode.collectAsStateWithLifecycle()
    val currentTheme = viewModel.currentTheme.collectAsStateWithLifecycle()

    val colorScheme = when (currentTheme.value) {
        AppTheme.BLUE -> if (isDark.value) darkBlueScheme else lightBlueScheme
        AppTheme.GREEN -> if (isDark.value) darkGreenScheme else lightGreenScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDark.value
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MelodiaTypography,
        content = content
    )

}