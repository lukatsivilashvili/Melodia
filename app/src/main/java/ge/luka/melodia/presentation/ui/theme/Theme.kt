package ge.luka.melodia.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.darkBlueScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.darkGreenScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.lightBlueScheme
import ge.luka.melodia.presentation.ui.theme.colors.ColorSchemes.lightGreenScheme

@Composable
fun MelodiaTheme(
    isDarkTheme: Boolean = false,
    userColorScheme: AppTheme = AppTheme.Green,
    content: @Composable () -> Unit
) {
    val colorScheme = when (userColorScheme) {
        AppTheme.Blue -> if (isDarkTheme) darkBlueScheme else lightBlueScheme
        AppTheme.Green -> if (isDarkTheme) darkGreenScheme else lightGreenScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MelodiaTypography,
        content = content
    )

}