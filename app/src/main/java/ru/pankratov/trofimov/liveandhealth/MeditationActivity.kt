package ru.pankratov.trofimov.liveandhealth

import android.app.Activity
import android.app.ProgressDialog
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_LINK_AUDIO_MEDITATION_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_LINK_IMAGE_MEDITATION_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_NAME_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_TAG
import java.io.IOException

class MeditationActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var seekBar: SeekBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        // get data from main activity intent
        val intent = intent
        val index = intent.getIntExtra(WORKOUT_TAG, 0)
        val nameMeditation = intent.getStringExtra(WORKOUT_NAME_TAG)
        val pathSong = intent.getStringExtra(WORKOUT_LINK_AUDIO_MEDITATION_TAG)
        val pathImg = intent.getStringExtra(WORKOUT_LINK_IMAGE_MEDITATION_TAG)


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

            // display title
            (findViewById<View>(R.id.text_name_meditation) as TextView).text = nameMeditation
            /// Load cover image (we use Picasso Library)

            // Get image view
            val mImageView = findViewById<View>(R.id.coverImage) as ImageView
            // Image url
            Picasso.with(applicationContext).load(pathImg).into(mImageView)

            // execute this code at the end of asynchronous media player preparation
            mediaPlayer!!.setOnPreparedListener { mp -> //start media player
                mp.start()
                // link seekbar to bar view
                seekBar = findViewById<View>(R.id.seekBar) as SeekBar
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

    fun play(view: View?) {
        mediaPlayer!!.start()
    }

    fun pause(view: View?) {
        mediaPlayer!!.pause()
    }

    fun stop(view: View?) {
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

    override fun onBackPressed() {
        super.onBackPressed()
        if (mediaPlayer != null) {
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        finish()
    }

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

}