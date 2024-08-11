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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.presentation.ui.theme.AppTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit,
    onUpdateTheme: (Pair<AppTheme, Boolean>) -> Unit
) {
    val previousRoute =
        navHostController.previousBackStackEntry?.destination?.route?.getScreenFromRoute()
    var isDarkTheme by remember { mutableStateOf(false) }
    var currentTheme by remember { mutableStateOf<AppTheme>(AppTheme.Green) }

    BackHandler {
        onUpdateRoute.invoke(previousRoute)
        navHostController.popBackStack()
    }

    Column(modifier = Modifier) {
        Row {
            Text("Dark mode")
            Switch(
                checked = isDarkTheme,
                onCheckedChange = {
                    isDarkTheme = it
                    onUpdateTheme.invoke(Pair(currentTheme, isDarkTheme))
                }
            )
        }
        Spacer(Modifier.height(16.dp))

        ColorSchemeRadioButtons(currentTheme) { newTheme ->
            currentTheme = newTheme
            onUpdateTheme.invoke(Pair(currentTheme, isDarkTheme))
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
            selected = selectedScheme == AppTheme.Green,
            onClick = { onSchemeSelected(AppTheme.Green) }
        )
        Text("Green")

        Spacer(Modifier.width(16.dp))

        RadioButton(
            selected = selectedScheme == AppTheme.Blue,
            onClick = { onSchemeSelected(AppTheme.Blue) }
        )
        Text("Blue")


    }
}