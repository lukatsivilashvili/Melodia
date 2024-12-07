package ge.luka.melodia.presentation.ui.components

import android.Manifest
import android.content.Context
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.common.mvi.CollectSideEffects
import ge.luka.melodia.presentation.ui.MelodiaScreen
import ge.luka.melodia.presentation.ui.components.singlepermission.PermissionAction
import ge.luka.melodia.presentation.ui.components.singlepermission.PermissionSideEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
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
    val showRationalDialog = remember { mutableStateOf(false) }

    CollectSideEffects(flow = viewModel.sideEffect) { effect ->
        when (effect) {
            is PermissionSideEffect.PermissionGranted -> {
                viewModel.cacheData()
                viewModel.createMediaDirectory(context = context)
            }
        }
    }

    val permissionState = createPermissionState(
        scope = scope,
        context = context,
        viewModel = viewModel,
        navHostController = navHostController,
        onUpdateRoute = onUpdateRoute
    )

    PermissionRequestContent(
        permissionState = permissionState,
        showRationalDialog = showRationalDialog,
        context = context
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun createPermissionState(
    scope: CoroutineScope,
    context: Context,
    viewModel: SinglePermissionViewModel,
    navHostController: NavHostController,
    onUpdateRoute: (String?) -> Unit
): PermissionState {
    val permissionCallback: (Boolean) -> Unit = { isGranted ->
        if (isGranted) {
            scope.launch {
                try {
                    viewModel.onAction(PermissionAction.PermissionGranted)
                    delay(500)
                    navHostController.navigate(MelodiaScreen.Library)
                    onUpdateRoute(MelodiaScreen.Library.toString().getScreenFromRoute())
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error initializing: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = Manifest.permission.READ_MEDIA_AUDIO,
            onPermissionResult = permissionCallback
        )
    } else {
        rememberPermissionState(
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            onPermissionResult = permissionCallback
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionRequestContent(
    permissionState: PermissionState,
    showRationalDialog: MutableState<Boolean>,
    context: Context
) {
    if (showRationalDialog.value) {
        PermissionRationalDialog(
            onDismiss = { showRationalDialog.value = false },
            onConfirm = {
                showRationalDialog.value = false
                launchAppSettings(context)
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        PermissionRequestBody(
            permissionState = permissionState,
            showRationalDialog = showRationalDialog,
            context = context
        )
    }
}

@Composable
private fun PermissionRationalDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Storage Permission Required",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Text(
                "Storage access is required to read your media files. Please grant the permission.",
                fontSize = 16.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK", style = TextStyle(color = Color.Black))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", style = TextStyle(color = Color.Black))
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionRequestBody(
    permissionState: PermissionState,
    showRationalDialog: MutableState<Boolean>,
    context: Context
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PermissionButton(
            permissionState = permissionState,
            showRationalDialog = showRationalDialog,
            context = context
        )

        PermissionStatusText(permissionState = permissionState)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionButton(
    permissionState: PermissionState,
    showRationalDialog: MutableState<Boolean>,
    context: Context
) {
    Button(
        onClick = {
            if (!permissionState.status.isGranted) {
                if (permissionState.status.shouldShowRationale) {
                    showRationalDialog.value = true
                } else {
                    permissionState.launchPermissionRequest()
                }
            } else {
                Toast.makeText(context, "Storage Permission Already Granted", Toast.LENGTH_SHORT).show()
            }
        },
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary,
            disabledContentColor = MaterialTheme.colorScheme.onTertiary,
        )
    ) {
        Text(text = "Request Storage Permission")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionStatusText(permissionState: PermissionState) {
    if (permissionState.status.shouldShowRationale) {
        Text("Storage access is required to read your media files. Please grant the permission.")
    } else {
        Text("Storage permission is required to access your media files.")
    }
}

private fun launchAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(context, intent, null)
}