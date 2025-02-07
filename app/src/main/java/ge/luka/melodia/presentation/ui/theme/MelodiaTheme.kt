package ge.luka.melodia.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.darkBlueScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.darkGreenScheme
import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme
import ge.luka.melodia.presentation.ui.theme.themecomponents.MelodiaTypography

@Composable
fun MelodiaTheme(
    viewState: ThemeViewState = ThemeViewState(
        currentTheme = AppTheme.GREEN, // Default theme
    ),
    content: @Composable () -> Unit,
) {
    val colorScheme = when (viewState.currentTheme) {
        AppTheme.BLUE ->darkBlueScheme
        AppTheme.GREEN ->darkGreenScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MelodiaTypography,
        content = content
    )
}