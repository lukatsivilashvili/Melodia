package ge.luka.melodia.presentation.ui.components

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.presentation.ui.MelodiaScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SinglePermissionRequest(
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
) {
    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.READ_MEDIA_AUDIO)
    } else {
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val context = LocalContext.current

    val showRationalDialog = remember { mutableStateOf(false) }
    if (showRationalDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showRationalDialog.value = false
            },
            title = {
                Text(
                    text = "Permission",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    "The notification is important for this app. Please grant the permission.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationalDialog.value = false
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(context, intent, null)

                    }) {
                    Text("OK", style = TextStyle(color = Color.Black))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRationalDialog.value = false
                    }) {
                    Text("Cancel", style = TextStyle(color = Color.Black))
                }
            },
        )
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                if (!permissionState.status.isGranted) {
                    if (permissionState.status.shouldShowRationale) {
                        // Show a rationale if needed (optional)
                        showRationalDialog.value = true
                    } else {
                        // Request the permission
                        permissionState.launchPermissionRequest()

                    }
                } else {
                    Toast.makeText(context, "Permission Given Already", Toast.LENGTH_SHORT)
                        .show()
                }
            },
                colors = ButtonColors(containerColor = MaterialTheme.colorScheme.primary,
                    contentColor =MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor =MaterialTheme.colorScheme.tertiary,
                    disabledContentColor =MaterialTheme.colorScheme.onTertiary,)
            ) {
                Text(text = "Ask for permission")
            }
            LaunchedEffect(key1 = permissionState.status.isGranted) {
                if (permissionState.status.isGranted) {
                    navHostController.navigate(MelodiaScreen.Library)
                    onUpdateRoute(MelodiaScreen.Library.toString().getScreenFromRoute())
                }
            }
            if (permissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                Text("The notification is important for this app. Please grant the permission.")
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                Text("The notification permission is required for some functionality.")
            }

        }
    }
}