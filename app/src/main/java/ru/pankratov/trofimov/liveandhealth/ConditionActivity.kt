package ru.pankratov.trofimov.liveandhealth

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.annotation.RequiresApi
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.FOCUS_TAG
import ru.pankratov.trofimov.liveandhealth.fragments.ConditionsFragment.ConditionObjects.DRAWABLES_ICONS_FOCUS
import ru.pankratov.trofimov.liveandhealth.fragments.ConditionsFragment.ConditionObjects.DRAWABLES_IMG_FOCUS
import ru.pankratov.trofimov.liveandhealth.fragments.ConditionsFragment.ConditionObjects.NAMES_FOCUS
import android.widget.VideoView
import android.net.Uri


class ConditionActivity : AppCompatActivity() {

    private lateinit var mTextName: TextView
    private lateinit var mImageHead: ImageView
    private lateinit var mImageCategory: ImageView

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
        mImageHead = findViewById(R.id.image_head_condition)
        mImageCategory = findViewById(R.id.image_category_condition)

        val intent = intent
        val id = intent.getIntExtra(FOCUS_TAG, 0)

        mTextName.text = NAMES_FOCUS[id]
        mImageHead.setImageResource(DRAWABLES_IMG_FOCUS[id])
        mImageCategory.setImageResource(DRAWABLES_ICONS_FOCUS[id])

//        val videoSource = "file:///android_asset//watervideo.mp4"

        val videoView = findViewById<View>(R.id.videoView) as VideoView

        val videoSource = "android.resource://ru.pankratov.trofimov.liveandhealth/" + R.raw.watervideo
        videoView.setVideoURI(Uri.parse(videoSource))
// более универсальный вариант
// videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/"+R.raw.cat));



//        videoView.setVideoPath(videoSource)

        videoView.setMediaController(MediaController(this))
        videoView.requestFocus(0)
        videoView.start() // начинаем воспроизведение автоматически

    }
}