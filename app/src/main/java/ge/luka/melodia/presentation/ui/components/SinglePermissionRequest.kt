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
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import ge.luka.melodia.common.extensions.getScreenFromRoute
import ge.luka.melodia.common.mvi.CollectSideEffects
import ge.luka.melodia.presentation.ui.MelodiaScreen
import ge.luka.melodia.presentation.ui.components.singlepermission.PermissionAction
import ge.luka.melodia.presentation.ui.components.singlepermission.PermissionSideEffect
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

    CollectSideEffects(flow = viewModel.sideEffect) { effect ->
        when (effect) {
            is PermissionSideEffect.PermissionGranted -> {
                viewModel.cacheData()
            }
        }
    }

    // Create a permission state with a callback
    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = Manifest.permission.READ_MEDIA_AUDIO,
            onPermissionResult = { isGranted ->
                if (isGranted) {
                    scope.launch {
                        try {
                            // Call repository immediately when permission is granted
                            viewModel.onAction(PermissionAction.PermissionGranted)

                            // Navigate after repository call succeeds
                            navHostController.navigate(MelodiaScreen.Library)
                            onUpdateRoute(MelodiaScreen.Library.toString().getScreenFromRoute())
                        } catch (e: Exception) {
                            // Handle any repository errors
                            Toast.makeText(
                                context,
                                "Error initializing: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        )
    } else {
        rememberPermissionState(
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            onPermissionResult = { isGranted ->
                if (isGranted) {
                    scope.launch {
                        try {
                            // Call repository immediately when permission is granted
                            viewModel.onAction(PermissionAction.PermissionGranted)

                            // Navigate after repository call succeeds
                            navHostController.navigate(MelodiaScreen.Library)
                            onUpdateRoute(MelodiaScreen.Library.toString().getScreenFromRoute())
                        } catch (e: Exception) {
                            // Handle any repository errors
                            Toast.makeText(
                                context,
                                "Error initializing: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        )
    }

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
            Button(
                onClick = {
                    if (!permissionState.status.isGranted) {
                        if (permissionState.status.shouldShowRationale) {
                            showRationalDialog.value = true
                        } else {
                            // Launch permission request - callback will be triggered on result
                            permissionState.launchPermissionRequest()
                        }
                    } else {
                        Toast.makeText(context, "Permission Given Already", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiary,
                )
            ) {
                Text(text = "Ask for permission")
            }

            if (permissionState.status.shouldShowRationale) {
                Text("The notification is important for this app. Please grant the permission.")
            } else {
                Text("The notification permission is required for some functionality.")
            }
        }
    }
}