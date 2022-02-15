package ru.pankratov.trofimov.liveandhealth

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.FOCUS_TAG
import ru.pankratov.trofimov.liveandhealth.fragments.ConditionsFragment.ConditionObjects.DRAWABLES_ICONS_FOCUS
import ru.pankratov.trofimov.liveandhealth.fragments.ConditionsFragment.ConditionObjects.DRAWABLES_IMG_FOCUS
import android.widget.*
import android.widget.MediaController
import android.widget.VideoView

class ConditionActivity : AppCompatActivity() {

    private lateinit var mTextName: TextView
    private lateinit var mTextDiscription: TextView
    private lateinit var mImageHead: ImageView
    private lateinit var mImageCategory: ImageView
    private lateinit var mVideoView: VideoView
    private lateinit var mBtnStartVideo: Button

    private val position = 0
    private var mediaController: MediaController? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_condition)

        mTextName = findViewById(R.id.text_name_condition)
        mTextDiscription = findViewById(R.id.text_discription_full_condition)
        mImageHead = findViewById(R.id.image_head_condition)
        mImageCategory = findViewById(R.id.image_category_condition)
        mVideoView = findViewById(R.id.videoView)
        mBtnStartVideo = findViewById(R.id.button_start)

        val intent = intent
        val id = intent.getIntExtra(FOCUS_TAG, 0)

        val listNames = resources.getStringArray(R.array.list_focus_exercise_array)
        val listDiscriptions = resources.getStringArray(R.array.list_focus_description_full_array)

        mTextName.text = listNames[id]
        mTextDiscription.text = listDiscriptions[id]
        mImageHead.setImageResource(DRAWABLES_IMG_FOCUS[id])
        mImageCategory.setImageResource(DRAWABLES_ICONS_FOCUS[id])
        // + фирменный шрифт
        val fontApp = Typeface.createFromAsset(assets, "Comfortaa-Bold.ttf")
        mTextName.typeface = fontApp
        mTextDiscription.typeface = fontApp

        if (mediaController == null) {
            mediaController = MediaController(this)

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController!!.setAnchorView(mVideoView)

            // Set MediaController for VideoView
            mVideoView.setMediaController(mediaController)
        }

        mVideoView.setOnPreparedListener { mediaPlayer ->
            mVideoView.seekTo(position)
            if (position == 0) {
                mVideoView.start()
            }

            // When video Screen change size.
            mediaPlayer.setOnVideoSizeChangedListener { mp, width, height -> // Re-Set the videoView that acts as the anchor for the MediaController
                mediaController!!.setAnchorView(mVideoView)
            }
        }
        mBtnStartVideo.setOnClickListener {
            val resName: String = VideoViewUtils.RAW_VIDEO_SAMPLE
            val video = VideoViewUtils()
            video.playRawVideo(this, mVideoView, resName)
        }


    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", mVideoView.currentPosition)
        mVideoView.pause()
    }


    // After rotating the phone. This method is called.
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Get saved position.
        val position = savedInstanceState.getInt("CurrentPosition")
        mVideoView.seekTo(position)
    }
}