package hu.mczinke.experimental_compose_stream

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.mczinke.experimental_compose_stream.ui.theme.ExperimentalComposeStreamTheme
import org.koin.androidx.compose.koinViewModel

private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExperimentalComposeStreamTheme {
                // A surface container using the 'background' color from the theme
                val viewModel = koinViewModel<StreamViewModel>()
                val streamer by viewModel.streamer.collectAsState()

                LaunchedEffect(
                    key1 = streamer,
                    block = {
                        Log.d(TAG, "onCreate: Streamer changes: $streamer")
                    }
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RequestPermissionScreen(
                        modifier = Modifier.fillMaxSize(),
                        streamer = streamer,
                        onPermissionGranted = viewModel::initStream,
                        onStreamButtonClick = viewModel::startOrStopStream
                    )
                }
            }
        }
    }
}