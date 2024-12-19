package ge.luka.melodia.presentation.ui.components.shared

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import ge.luka.melodia.domain.model.BaseModel

@Composable
fun MetadataDialog(
    audioModel: BaseModel?,
    onDismiss: () -> Unit,
    shouldShowAlbumField: Boolean,
    onSave: (id: Long, title: String, artist: String, album: String?, artworkUri: String?) -> Unit
) {
    var title by remember { mutableStateOf(audioModel?.title ?: "") }
    var artist by remember { mutableStateOf(audioModel?.artist ?: "") }
    var album by remember { mutableStateOf(audioModel?.album ?: "") }
    var artworkUri by remember { mutableStateOf(audioModel?.artUri ?: "") }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            // Persist URI permissions
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                e.printStackTrace() // Handle exception if permission cannot be persisted
            }
            // Store the URI as a string
            artworkUri = it.toString()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Metadata") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Album Art
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(artworkUri)
                                .build()
                        ),
                        contentDescription = "Album Art",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Change Art Button
                OutlinedButton(
                    onClick = { imagePickerLauncher.launch(arrayOf("image/*")) }
                ) {
                    Text("Change Album Art")
                }

                // Metadata Fields
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = artist,
                    onValueChange = { artist = it },
                    label = { Text("Artist") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (shouldShowAlbumField) {
                    OutlinedTextField(
                        value = album,
                        onValueChange = { album = it },
                        label = { Text("Album") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        if (shouldShowAlbumField) audioModel?.songId ?: 0 else audioModel?.albumId
                            ?: 0,
                        title,
                        artist,
                        album,
                        artworkUri
                    )
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}