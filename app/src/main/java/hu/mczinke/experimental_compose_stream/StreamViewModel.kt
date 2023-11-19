package hu.mczinke.experimental_compose_stream

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.thibaultbee.streampack.data.VideoConfig
import io.github.thibaultbee.streampack.ext.srt.streamers.CameraSrtLiveStreamer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StreamViewModel(
    application: Application
): AndroidViewModel(application) {

    private val _streamer: MutableStateFlow<CameraSrtLiveStreamer?> = MutableStateFlow(null)
    val streamer = _streamer.asStateFlow()

    fun startOrStopStream() {
        if(streamer.value?.isConnected == true) {
            stopStream()
        }else {
            //here should have some kind of timer to block instant stop
            startStream()
        }
    }
    fun initStream() {
        viewModelScope.launch {
            val streamer = CameraSrtLiveStreamer(
                getApplication(),
                false
            ).apply {
                configure(VideoConfig())
            }
            _streamer.emit(streamer)
        }
    }
    private fun stopStream() {
        streamer.value?.disconnect()
    }

    private fun startStream() {
        viewModelScope.launch {
            streamer.value?.startStream("192.168.50.153", 4000)
        }
    }
}