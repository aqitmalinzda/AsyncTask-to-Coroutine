package perangkaikode.com.asynctasktocoroutine

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sample.*
import kotlinx.coroutines.*

class SampleAct : AppCompatActivity() {

    private var mTask: MyAsync? = null
    private var job: Job? = null

    private var duration = 0
    private var current: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        bt_play?.setOnClickListener {
//            playVideo("Tulis URL Video disini")
//            playVideoWithCoroutine("")
        }
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
    }

    private fun playVideoWithCoroutine(uriPath: String?){
        GlobalScope.launch(Dispatchers.Main) {
            pb_video?.progress = 0
            pb_video?.max = 100

            vv_sample?.setVideoPath(uriPath)
            videoTask()
        }
    }

    private suspend fun videoTask() = withContext(Dispatchers.Default){
        job = launch {
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
                    updateProgress((current!! * 100 / duration))
                    if (pb_video?.progress!! >= 100) {
                        break
                    }
                } catch (e: Exception) {
                    /**/
                }
            } while (pb_video?.progress!! <= 100)
        }
    }

    private fun updateProgress(progress: Int){
        pb_video?.progress = progress
    }

    private fun playVideo(uriPath: String?) {
        if (uriPath != null) {
            pb_video?.progress = 0
            pb_video?.max = 100

            vv_sample?.setVideoPath(uriPath)

            mTask = MyAsync()
            mTask?.execute()
        }
    }

    inner class MyAsync : AsyncTask<Void, Int, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
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
                    publishProgress((current!! * 100 / duration))
                    if (pb_video?.progress!! >= 100) {
                        break
                    }
                } catch (e: Exception) {
                }
                if (isCancelled) {
                    break
                }
            } while (pb_video?.progress!! <= 100)

            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            pb_video?.progress = values[0]!!
        }
    }
}
