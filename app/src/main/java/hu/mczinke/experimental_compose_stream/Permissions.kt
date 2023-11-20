package hu.mczinke.experimental_compose_stream

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import io.github.thibaultbee.streampack.streamers.interfaces.ICameraStreamer

private const val TAG = "Permissions"
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionScreen(
    modifier: Modifier = Modifier,
    streamer: ICameraStreamer? = null,
    onPermissionGranted: () -> Unit,
    onStreamButtonClick: () -> Unit,
    streamerType: StreamerType,
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
        )
    )

    LaunchedEffect(
        key1 = multiplePermissionsState,
        block =  {
            if(multiplePermissionsState.allPermissionsGranted) {
                Log.d(TAG, "RequestPermissionScreen: permission is granted")
                onPermissionGranted()
            }
        }
    )

    val cameraPermission = rememberPermissionState(permission = Manifest.permission.CAMERA)

    if(multiplePermissionsState.allPermissionsGranted) {
//        val streamer = rememberStreamer()

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            when(streamerType) {
                StreamerType.Camera -> {
                    if(streamer != null) {
                        PreviewView(
                            modifier = Modifier.fillMaxSize(.9f),
                            streamer = streamer
                        )
                    }else {
                        Box(modifier = Modifier.fillMaxSize(.9f)) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Center),
                                text = "Streamer is null"
                            )
                        }
                    }
                    Button(onClick = onStreamButtonClick) {
                        Text(text = "Start / Stop")
                    }
                }
                StreamerType.Ar -> {
                    ArPreviewView(
                        modifier = Modifier.fillMaxSize(.9f),
                    )
                    Button(onClick = onStreamButtonClick) {
                        Text(text = "Start / Stop")
                    }
                }
            }
        }

    } else {
        Column {
            Text(
                getTextToShowGivenPermissions(
                    multiplePermissionsState.revokedPermissions,
                    multiplePermissionsState.shouldShowRationale
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                Text("Request permissions")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun getTextToShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean
): String {
    val revokedPermissionsSize = permissions.size
    if (revokedPermissionsSize == 0) return ""

    val textToShow = StringBuilder().apply {
        append("The ")
    }

    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission)
        when {
            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
                textToShow.append(", and ")
            }
            i == revokedPermissionsSize - 1 -> {
                textToShow.append(" ")
            }
            else -> {
                textToShow.append(", ")
            }
        }
    }
    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
    textToShow.append(
        if (shouldShowRationale) {
            " important. Please grant all of them for the app to function properly."
        } else {
            " denied. The app cannot function without them."
        }
    )
    return textToShow.toString()
}