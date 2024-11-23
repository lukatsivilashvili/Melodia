package ge.luka.melodia.presentation.ui.screens.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ge.luka.melodia.presentation.ui.theme.themecomponents.AppTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsScreenVM = hiltViewModel(),
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit,
) {

    onUpdateRoute.invoke("Settings")

    val viewState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        navHostController.popBackStack()
    }

    Column(modifier = Modifier) {
        Row {
            Text("Dark mode")
            Switch(
                checked = viewState.darkMode,
                onCheckedChange = {
                    viewModel.onAction(SettingsAction.DarkModeSwitched(darkMode = viewState.darkMode.not()))
                }
            )
        }
        Spacer(Modifier.height(16.dp))

        ColorSchemeRadioButtons(selectedScheme = viewState.currentTheme) { newTheme ->
            viewModel.onAction(SettingsAction.ThemeChanged(newTheme = newTheme))
        }
    }
}


@Composable
fun ColorSchemeRadioButtons(
    selectedScheme: AppTheme,
    onSchemeSelected: (AppTheme) -> Unit
) {
    Row {

        RadioButton(
            selected = selectedScheme == AppTheme.GREEN,
            onClick = { onSchemeSelected(AppTheme.GREEN) }
        )
        Text("Green")

        Spacer(Modifier.width(16.dp))

        RadioButton(
            selected = selectedScheme == AppTheme.BLUE,
            onClick = { onSchemeSelected(AppTheme.BLUE) }
        )
        Text("Blue")
    }
}