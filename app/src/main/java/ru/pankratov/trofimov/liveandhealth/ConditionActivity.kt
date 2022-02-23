package ru.pankratov.trofimov.liveandhealth

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.FOCUS_TAG
import ru.pankratov.trofimov.liveandhealth.fragments.ConditionsFragment.ConditionObjects.DRAWABLES_ICONS_FOCUS
import ru.pankratov.trofimov.liveandhealth.fragments.ConditionsFragment.ConditionObjects.DRAWABLES_IMG_FOCUS
import android.widget.*
import android.widget.MediaController
import android.widget.VideoView
import ru.pankratov.trofimov.liveandhealth.controls.VideoViewCustom
import ru.pankratov.trofimov.liveandhealth.controls.VideoViewUtils

class ConditionActivity : AppCompatActivity() {

    private lateinit var mTextName: TextView
    private lateinit var mTextDiscription: TextView
    private lateinit var mImageHead: ImageView
    private lateinit var mImageCategory: ImageView
    private lateinit var mVideoView: VideoView
    private lateinit var mBtnStartVideo: Button

    private val position = 0
    private var mediaController: MediaController? = null

    var videoCustomClass: VideoViewCustom? = null

    var displayWidth = getScreenWidth(this)
    var displayHeight = getScreenHeigth(this)
    val smallHeight = 300

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_condition)

        videoCustomClass = VideoViewCustom(this)

        displayWidth = getScreenWidth(this)
        displayHeight = getScreenHeigth(this)

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
            // Устанавливаем videoView, в MediaController
            mediaController!!.setAnchorView(mVideoView)
            // Устанавливаем MediaController в VideoView
            mVideoView.setMediaController(mediaController)
        }

        mVideoView.setOnPreparedListener { mediaPlayer ->
            mVideoView.seekTo(position)
            if (position == 0) {
                mVideoView.start()
            }

            // Смена размера экрана.
            mediaPlayer.setOnVideoSizeChangedListener { mp, width, height ->
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
        // Сохранить текущую позицию
        savedInstanceState.putInt("CurrentPosition", mVideoView.currentPosition)
        mVideoView.pause()
    }

    // После поворота телефона. Этот метод называется
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Получить сохраненную позицию
        val position = savedInstanceState.getInt("CurrentPosition")
        mVideoView.seekTo(position)
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
//    }



    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            videoCustomClass?.setDimensions(displayHeight, displayWidth)
            videoCustomClass?.holder?.setFixedSize(displayHeight, displayWidth)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
            )
            videoCustomClass?.setDimensions(displayWidth, smallHeight)
            videoCustomClass?.holder?.setFixedSize(displayWidth, smallHeight)
        }
    }

    // получаем ширину экрана
    private fun getScreenWidth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }
    // получаем высоту экрана
    private fun getScreenHeigth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }
}