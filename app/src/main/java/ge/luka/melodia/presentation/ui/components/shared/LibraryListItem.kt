package ge.luka.melodia.presentation.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ge.luka.melodia.presentation.ui.theme.MelodiaTheme

@Composable
fun LibraryListItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(imageVector = icon, contentDescription = "LibraryListItem")
                    Text(
                        modifier = modifier.padding(start = 8.dp),
                        text = title
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "ArrowForwardIcon"
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(1.dp)
                    .background(Color.Black)
            )
        }
    }
}

@Preview
@Composable
fun LibraryListItemPreview(modifier: Modifier = Modifier) {
    MelodiaTheme {
        LibraryListItem(
            title = "Playlist", icon = Icons.Filled.Build
        )

    }

}