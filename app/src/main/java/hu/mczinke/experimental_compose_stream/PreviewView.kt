package hu.mczinke.experimental_compose_stream

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.github.thibaultbee.streampack.streamers.interfaces.ICameraStreamer
import io.github.thibaultbee.streampack.views.PreviewView

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