package ru.pankratov.trofimov.liveandhealth

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.RequiresApi
import com.squareup.picasso.Picasso
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.MEDITATION_TAG
import ru.pankratov.trofimov.liveandhealth.dialogs.StartBreathDialog
import ru.pankratov.trofimov.liveandhealth.dialogs.StartMeditationDialog
import ru.pankratov.trofimov.liveandhealth.services.AudioService
import java.io.IOException

class MeditationActivity : AppCompatActivity() {

    private lateinit var mBtnPlayPause: ImageButton
    private lateinit var mTextNameBreath: TextView
    private lateinit var mTextDiscriptionFullBreath: TextView

    private var mediaPlayer: MediaPlayer? = null
    private var seekBar: SeekBar? = null

    var playPause = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (mediaPlayer != null) {
                    mediaPlayer!!.reset()
                    mediaPlayer!!.release()
                    mediaPlayer = null
                }
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // get data from main activity intent
        val intent = intent
        val index = intent.getIntExtra(MEDITATION_TAG, 0)

        // медитации - заполняем списки ссылок на название, аудио и картинки
        val listName = resources.getStringArray(R.array.list_meditation_exercise_array)
        val listDiscr = resources.getStringArray(R.array.list_meditation_description_array)
        val listAudioMeditationArray = resources.getStringArray(R.array.list_meditation_audio_array)
        val listImageMeditationArray = resources.getStringArray(R.array.list_meditation_image_array)
        // получаем нужное из списков
        val nameMeditation = listName[index]
        val discriptionMeditation = listDiscr[index]
        val pathSong = listAudioMeditationArray[index]
        val pathImg = listImageMeditationArray[index]

        // create a media player
        mediaPlayer = MediaPlayer()

        // try to load data and play
        try {
            // give data to mediaPlayer
            mediaPlayer!!.setDataSource(pathSong)
            // media player asynchronous preparation
            mediaPlayer!!.prepareAsync()
            // create a progress dialog (waiting media player preparation)
            val dialog = ProgressDialog(this@MeditationActivity)
            // set message of the dialog
            dialog.setMessage(getString(R.string.loading))
            // prevent dialog to be canceled by back button press
            dialog.setCancelable(false)
            // show dialog at the bottom
            dialog.window!!.setGravity(Gravity.CENTER)
            // show dialog
            dialog.show()

            // inflate layout
            setContentView(R.layout.activity_meditation)

            supportActionBar?.hide()

            // название и описание
            mTextNameBreath = findViewById(R.id.text_name_meditation)
            mTextNameBreath.text = nameMeditation
            mTextDiscriptionFullBreath = findViewById(R.id.text_discription_full_meditation)
            mTextDiscriptionFullBreath.text = discriptionMeditation
            // + фирменный шрифт
            val fontApp = Typeface.createFromAsset(assets, "Comfortaa-Bold.ttf")
            mTextNameBreath.typeface = fontApp
            mTextDiscriptionFullBreath.typeface = fontApp

            mBtnPlayPause = findViewById(R.id.play_pause)
            mBtnPlayPause.setOnClickListener {
                playPauseBtn()
            }

            /// Load cover image (we use Picasso Library)

            // Get image view
            val mImageView = findViewById<View>(R.id.coverImage) as ImageView
            // Image url
            Picasso.with(applicationContext).load(pathImg).into(mImageView)

            // execute this code at the end of asynchronous media player preparation
            mediaPlayer!!.setOnPreparedListener { mp ->

                mediaPlayer!!.setVolume(VOLUME, VOLUME)
                // начинаем подготовительный диалог
                startDialog()
                // link seekbar to bar view
                seekBar = findViewById<View>(R.id.seekBar_meditation) as SeekBar
                //update seekbar
                mRunnable.run()
                //dismiss dialog
                dialog.dismiss()
            }
        } catch (e: IOException) {
            val a: Activity = this
            a.finish()
            Toast.makeText(this, getString(R.string.file_not_found), Toast.LENGTH_SHORT).show()
        }

    }

    private val mHandler = Handler()
    private val mRunnable: Runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null) {

                //set max value
                val mDuration = mediaPlayer!!.duration
                seekBar!!.max = mDuration

                //update total time text view
                val totalTime = findViewById<View>(R.id.totalTime) as TextView
                totalTime.text = getTimeString(mDuration.toLong())

                //set progress to current position
                val mCurrentPosition = mediaPlayer!!.currentPosition
                seekBar!!.progress = mCurrentPosition

                //update current time text view
                val currentTime = findViewById<View>(R.id.currentTime) as TextView
                currentTime.text = getTimeString(mCurrentPosition.toLong())

                //handle drag on seekbar
                seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onProgressChanged(
                        seekBar: SeekBar,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        if (mediaPlayer != null && fromUser) {
                            mediaPlayer!!.seekTo(progress)
                        }
                    }
                })
            }

            //repeat above code every second
            mHandler.postDelayed(this, 10)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun playPauseBtn() {
        playPause = when (playPause) {
            true -> {
                play()
                mBtnPlayPause.setImageResource(R.mipmap.ic_btn_pause_meditation)
                false
            }
            false -> {
                pause()
                mBtnPlayPause.setImageResource(R.mipmap.ic_btn_play_meditation)
                true
            }
        }
    }

    fun play() {
        mediaPlayer!!.start()
    }

    fun pause() {
        mediaPlayer!!.pause()
    }

    fun stop() {
        mediaPlayer!!.seekTo(0)
        mediaPlayer!!.pause()
    }

    fun seekForward(view: View?) {

        //set seek time
        val seekForwardTime = 5000

        // get current song position
        val currentPosition = mediaPlayer!!.currentPosition
        // check if seekForward time is lesser than song duration
        if (currentPosition + seekForwardTime <= mediaPlayer!!.duration) {
            // forward song
            mediaPlayer!!.seekTo(currentPosition + seekForwardTime)
        } else {
            // forward to end position
            mediaPlayer!!.seekTo(mediaPlayer!!.duration)
        }
    }

    fun seekBackward(view: View?) {
        //set seek time
        val seekBackwardTime = 5000
        // get current song position
        val currentPosition = mediaPlayer!!.currentPosition
        // check if seekBackward time is greater than 0 sec
        if (currentPosition - seekBackwardTime >= 0) {
            // forward song
            mediaPlayer!!.seekTo(currentPosition - seekBackwardTime)
        } else {
            // backward to starting position
            mediaPlayer!!.seekTo(0)
        }
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer!!.start()
    }

    // отображение времени
    private fun getTimeString(millis: Long): String {
        val buf = StringBuffer()
        val hours = millis / (1000 * 60 * 60)
        val minutes = millis % (1000 * 60 * 60) / (1000 * 60)
        val seconds = millis % (1000 * 60 * 60) % (1000 * 60) / 1000
        buf
            .append(String.format("%02d", hours))
            .append(":")
            .append(String.format("%02d", minutes))
            .append(":")
            .append(String.format("%02d", seconds))
        return buf.toString()
    }

    private fun startDialog() {
        try {
            val dialog = StartMeditationDialog()
            val manager = supportFragmentManager
            dialog.show(manager, "StartDialog")
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun startAudio() {
        val intent = Intent(this, AudioService::class.java)
        intent.putExtra(MainActivity.AUDIOFILE_BACKGROUND_TAG, R.raw.intromeditation)
        startService(intent)
    }

    fun stopAudio() {
        stopService(Intent(this, AudioService::class.java))
    }

    companion object {
        const val VOLUME = 0.9f
    }

}