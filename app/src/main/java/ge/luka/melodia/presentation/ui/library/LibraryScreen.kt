package ge.luka.melodia.presentation.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {
    LibraryScreenContent(modifier = modifier)
}

@Composable
fun LibraryScreenContent(
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {

    }
}