package ru.pankratov.trofimov.liveandhealth

import android.app.Activity
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import ru.pankratov.trofimov.liveandhealth.controls.VideoControllerView
import java.io.IOException


class VideoActivity : Activity(), SurfaceHolder.Callback, OnPreparedListener,
    VideoControllerView.MediaPlayerControl {

    private var videoSurface: SurfaceView? = null
    private var player: MediaPlayer? = null
    private var controller: VideoControllerView? = null
    private lateinit var container: FrameLayout

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        videoSurface = findViewById<View>(R.id.videoSurface) as SurfaceView
        val videoHolder = videoSurface!!.holder
        videoHolder.addCallback(this)

        // инициализируем контроллер и плеера для данной активности
        player = MediaPlayer()
        controller = VideoControllerView(this)
        container = findViewById(R.id.videoSurfaceContainer)

        // инициализируем нашего плеера и запускаем сходу видео как только запускается активность
        try {
            val path = Uri.parse("https://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
            val path2 =
                Uri.parse("android.resource://" + this.packageName.toString() + "/" + "firevideo")
//            player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player!!.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            player!!.setDataSource(this, path2)
            player!!.setOnPreparedListener(this)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // показываем контроллы по косанию к экрану, не к видео
    override fun onTouchEvent(event: MotionEvent): Boolean {
        controller!!.show()
        return false
    }

    // Implement SurfaceHolder.Callback
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    // подготавливаем видео к проигрыванию, как только загружаем его, показываем
    override fun surfaceCreated(holder: SurfaceHolder) {
        player!!.setDisplay(holder)
        player!!.prepareAsync()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    // End SurfaceHolder.Callback
    // Implement MediaPlayer.OnPreparedListener
    override fun onPrepared(mp: MediaPlayer) {
        controller!!.setMediaPlayer(this)
        controller!!.setAnchorView(findViewById<View>(R.id.videoSurfaceContainer) as FrameLayout)
        player!!.start()
    }

    // End MediaPlayer.OnPreparedListener
    // Implement VideoMediaController.MediaPlayerControl
    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override val bufferPercentage: Int
        get() = 0
    override val currentPosition: Int
        get() = player!!.currentPosition
    override val duration: Int
        get() = player!!.duration
    override val isPlaying: Boolean
        get() = player!!.isPlaying

    override fun pause() {
        player!!.pause()
    }

    override fun seekTo(i: Int) {
        player!!.seekTo(i)
    }

    override fun start() {
        player!!.start()
    }

    override val isFullScreen: Boolean
        get() = false

    override fun toggleFullScreen() {
        //обрабатываем нажатие на кнопку увеличения видео в весь экран
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val params = container.layoutParams as LinearLayout.LayoutParams
        params.width = metrics.widthPixels
        params.height = metrics.heightPixels
        params.leftMargin = 0
        container.layoutParams = params
    } // End VideoMediaController.MediaPlayerControl
}