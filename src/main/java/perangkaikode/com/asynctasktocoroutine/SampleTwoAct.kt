package perangkaikode.com.asynctasktocoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sample_two.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SampleTwoAct : AppCompatActivity(), CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var duration = 0
    private var current: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_two)

        bt_play?.setOnClickListener {
            playVideoWithCoroutine("")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancelChildren()
    }

    private fun playVideoWithCoroutine(uriPath: String?) = launch {
        pb_video?.progress = 0
        pb_video?.max = 100

        vv_sample?.setVideoPath(uriPath)

        videoTask()
    }

    private suspend fun videoTask() = withContext(Dispatchers.Default) {
        vv_sample?.setOnPreparedListener {
            duration = vv_sample?.duration!!
            vv_sample?.start()
        }

        do {
            current = if (current != null) {
                vv_sample?.currentPosition
            } else {
                0
            }
            try {
                pb_video?.progress = ((current!! * 100 / duration))
                if (pb_video?.progress!! >= 100) {
                    break
                }
            } catch (e: Exception) {
                /**/
            }
        } while (pb_video?.progress!! <= 100)
    }
}
