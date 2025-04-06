package ge.luka.melodia.presentation.ui.components.singlepermission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.common.mvi.CollectSideEffects
import ge.luka.melodia.domain.model.SongModel
import ge.luka.melodia.presentation.ui.screens.MelodiaScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SinglePermissionRequest(
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit,
    viewModel: SinglePermissionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val showRationaleDialog = remember { mutableStateOf(false) }
    val showSongScanDialog = remember { mutableStateOf(false) }
    val uiState = viewModel.uiState.collectAsState().value

    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            scope.launch {
                viewModel.onAction(PermissionAction.PermissionGranted(it.toString()))
                showSongScanDialog.value = true
            }
        }
    }

    val permission =
        rememberPermissionState(permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO
        else Manifest.permission.READ_EXTERNAL_STORAGE, onPermissionResult = { granted ->
            if (granted) {
                try {
                    folderPickerLauncher.launch(null)
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

    CollectSideEffects(flow = viewModel.sideEffect) { effect ->
        if (effect is PermissionSideEffect.PermissionGranted) {
            viewModel.startScan(context, effect.folderUri)
        }
    }

    if (showSongScanDialog.value) {
        ScanProgressDialog(
            scannedSongs = uiState.scanningState,
            onDismiss = {
                onUpdateRoute(MelodiaScreen.Library.toString().getScreenFromRoute())
                navHostController.navigate(MelodiaScreen.Library)
            },
            isButtonEnabled = uiState.scanningFinished
        )
    }

    if (showRationaleDialog.value) {
        RationaleDialog(onDismiss = { showRationaleDialog.value = false }, onConfirm = {
            showRationaleDialog.value = false
            openAppSettings(context)
        })
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                if (permission.status.isGranted) {
                    Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT).show()
                } else if (permission.status.shouldShowRationale) {
                    showRationaleDialog.value = true
                } else {
                    permission.launchPermissionRequest()
                }
            }) {
                Text("Request Storage Permission")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                if (permission.status.shouldShowRationale) "This app needs access to your media files. Please grant the permission."
                else "Storage permission is required to continue."
            )
        }
    }
}

@Composable
fun ScanProgressDialog(
    modifier: Modifier = Modifier,
    scannedSongs: List<SongModel>,
    onDismiss: () -> Unit,
    isButtonEnabled: Boolean
) {
    AlertDialog(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss, enabled = isButtonEnabled) {
                Text("Dismiss")
            }
        },
        title = { Text("Scanning for Songs") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                CircularProgressIndicator(modifier = modifier.align(Alignment.CenterHorizontally))
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Last found: ${scannedSongs.lastOrNull()?.title ?: "No songs found"}",
                    maxLines = 1
                )
                Text(text = "Total found: ${scannedSongs.size}")
            }
        }
    )
}

@Composable
private fun RationaleDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text("Permission Required", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = { Text("To access your media files, please grant storage permission.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("OK", color = Color.Black) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Black) }
        })
}

private fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    ContextCompat.startActivity(context, intent, null)
}