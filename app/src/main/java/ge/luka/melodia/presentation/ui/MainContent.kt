package ge.luka.melodia.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ge.luka.melodia.presentation.ui.components.MelodiaNavController

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    navController: NavHostController,
    updateCurrentRoute: (String?) -> Unit,
) {

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
                onUpdateRoute = updateCurrentRoute,
            )
        }
    }
}