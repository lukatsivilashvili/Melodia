package ge.luka.melodia.presentation.ui.components.shared

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun TransparentSurface(content: @Composable () -> Unit) {
    Surface(
        color = Color.Transparent, // Make the surface transparent
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.White) { // Force white content color
            content()
        }
    }
}