package ru.pankratov.trofimov.liveandhealth.services

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.AUDIOFILE_BACKGROUND_TAG


open class AudioService: Service() {

    var player: MediaPlayer? = null
    private var file = 0

    /*@Nullable*/
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        file = intent.getIntExtra(AUDIOFILE_BACKGROUND_TAG, 0)
        player = MediaPlayer.create(this, file)
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player!!.setVolume(volumeMusic, volumeMusic)
        player!!.isLooping = true //Зацикливаем player
        player!!.start()

        player = MediaPlayer().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setAudioAttributes(
                    AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
            }
            setDataSource(applicationContext, file)
            prepare()
            start()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        player!!.stop()
    }

//    override fun onStart(intent: Intent?, startId: Int) {
//        player!!.start()
//    }

    companion object {
        const val volumeMusic = 0.5f
    }

}