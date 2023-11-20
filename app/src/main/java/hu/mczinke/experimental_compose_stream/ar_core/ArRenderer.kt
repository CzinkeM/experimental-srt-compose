package hu.mczinke.experimental_compose_stream.ar_core

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.google.ar.core.Frame
import com.google.ar.core.Session
import com.google.ar.core.exceptions.NotYetAvailableException
import com.google.ar.core.exceptions.SessionPausedException
import java.io.IOException
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ArRenderer(
    val context: Context,
    val session: Session?,
    val displayRotationHelper: DisplayRotationHelper,
    val onFrameAvailableCallback: (Frame) -> Unit
): GLSurfaceView.Renderer {
    private val cameraImageRenderer = BackgroundRenderer()

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0.1f,0.1f,0.1f,1.0f)
        try {
            cameraImageRenderer.createOnGlThread(context)
        } catch (e: IOException) {
            throw e
        }
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        displayRotationHelper.onSurfaceChanged(width, height)
        GLES20.glViewport(0,0,width,height)
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        if(session == null) {
            return
        }
        displayRotationHelper.updateSessionIfNeeded(session)
        try {
            session.setCameraTextureName(cameraImageRenderer.textureId)
            val frame = session.update()
            onFrameAvailableCallback(frame)
            cameraImageRenderer.draw(frame)
        }catch (e: SessionPausedException) {
            return
        }catch (e: NotYetAvailableException) {
            return
        }catch (t: Throwable) {
            throw t
        }
    }
}