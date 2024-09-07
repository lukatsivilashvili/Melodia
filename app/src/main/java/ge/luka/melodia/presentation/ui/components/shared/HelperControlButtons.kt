package ge.luka.melodia.presentation.ui.components.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HelperControlButtons(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = modifier
                .weight(1f)
                .padding(start = 10.dp, end = 5.dp),
            shape = MaterialTheme.shapes.small
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play Button",
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(text = "Play", color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = modifier
                .weight(1f)
                .padding(end = 10.dp, start = 5.dp),
            shape = MaterialTheme.shapes.small

        ) {
            Icon(
                imageVector = Icons.Filled.Shuffle,
                contentDescription = "Play Button",
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(text = "Shuffle", color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelperControlButtonsPreview(modifier: Modifier = Modifier) {
    HelperControlButtons()
}