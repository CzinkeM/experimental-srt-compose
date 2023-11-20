package hu.mczinke.experimental_compose_stream

import android.opengl.GLSurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.ar.core.Session
import hu.mczinke.experimental_compose_stream.ar_core.ArRenderer
import hu.mczinke.experimental_compose_stream.ar_core.BackgroundRenderer
import hu.mczinke.experimental_compose_stream.ar_core.DisplayRotationHelper
import io.github.thibaultbee.streampack.streamers.interfaces.ICameraStreamer

@Composable
fun ArPreviewView(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val session = Session(context)
    val displayRotationHelper = DisplayRotationHelper(context)
    val renderer = ArRenderer(
        context,
        session,
        displayRotationHelper,
        {}
    )

    AndroidView(
        modifier = modifier,
        factory = {
            GLSurfaceView(it)
                .apply {
                    preserveEGLContextOnPause = true
                    setEGLContextClientVersion(2)
                    setEGLConfigChooser(8,8,8,8,16,0)
                    setRenderer(renderer)
                    renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
                    setWillNotDraw(true)
                }
                .also {
                    session.resume()
                }
        },
    )
}