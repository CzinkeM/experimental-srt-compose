package hu.mczinke.experimental_compose_stream

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.github.thibaultbee.streampack.error.StreamPackError
import io.github.thibaultbee.streampack.ext.srt.streamers.CameraSrtLiveStreamer
import io.github.thibaultbee.streampack.listeners.OnConnectionListener
import io.github.thibaultbee.streampack.listeners.OnErrorListener
import io.github.thibaultbee.streampack.streamers.interfaces.ICameraStreamer
import io.github.thibaultbee.streampack.views.PreviewView


@Composable
fun TestScreen(modifier: Modifier = Modifier) {
    val streamer = rememberStreamer()

    PreviewView(streamer = streamer)
}

@Composable
fun PreviewView(
    modifier: Modifier = Modifier,
    streamer: ICameraStreamer
) {
    AndroidView(
        modifier = modifier,
        factory = {
            PreviewView(it).apply {
                this.streamer = streamer
            }
        },
    )
}

@Composable
fun rememberStreamer(
    enableAudio: Boolean = false,
    errorListener: OnErrorListener = StreamerDefaults.ErrorListener,
    connectionListener: OnConnectionListener = StreamerDefaults.ConnectionListener,
): CameraSrtLiveStreamer {
    val context = LocalContext.current
    return remember {
        CameraSrtLiveStreamer(
            context = context,
            enableAudio = enableAudio,
            initialOnConnectionListener = connectionListener,
            initialOnErrorListener = errorListener,
        )
    }
}

object StreamerDefaults {

    val ErrorListener = object: OnErrorListener{
        override fun onError(error: StreamPackError) {}
    }

    val ConnectionListener = object : OnConnectionListener {
        override fun onLost(message: String) {}

        override fun onFailed(message: String) {}

        override fun onSuccess() {}
    }
}