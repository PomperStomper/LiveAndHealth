package ru.pankratov.trofimov.liveandhealth

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.pankratov.trofimov.liveandhealth.fragments.*


class MainActivity : AppCompatActivity() {

    private lateinit var mFocus: AppCompatButton
    private lateinit var mBreath: AppCompatButton
    private lateinit var mMeditation: AppCompatButton
    private lateinit var mSettings: AppCompatButton

    var fragment: Fragment = ConditionsFragment()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                when (fragmentFlag) {
                    0 -> onBackPressedDispatcher.onBackPressed()
                    1 -> {
                        fragment = ConditionsFragment()
                        fragmentAdd()
                    }
                    2 -> {
                        fragment = BreathsFragment()
                        fragmentAdd()
                    }
                    3 -> {
                        fragment = MeditationsFragment()
                        fragmentAdd()
                    }
                    4 -> {
                        fragment = SettingsFragment()
                        fragmentAdd()
                    }
                    else -> onBackPressedDispatcher.onBackPressed()
                }
                fragmentFlag = 0
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        mFocus = findViewById(R.id.btn_conditions_main)
        mBreath = findViewById(R.id.btn_breaths_main)
        mMeditation = findViewById(R.id.btn_meditations_main)
        mSettings = findViewById(R.id.btn_settings_main)
        mFocus.setOnClickListener {
            changeFragment(1)
        }
        mBreath.setOnClickListener {
            changeFragment(2)
        }
        mMeditation.setOnClickListener {
            changeFragment(3)
        }
        mSettings.setOnClickListener {
            changeFragment(4)
        }

        // + фирменный шрифт
        val fontAppZagolovok = Typeface.createFromAsset(assets, "Comfortaa-Regular.ttf")
        mFocus.typeface = fontAppZagolovok
        mBreath.typeface = fontAppZagolovok
        mMeditation.typeface = fontAppZagolovok
        mSettings.typeface = fontAppZagolovok


        startFirstFragment()
    }

    private fun startFirstFragment() {
        fragmentAdd()

        val white = ContextCompat.getColor(applicationContext, R.color.white)

        mFocus.setTextColor(white)
        mFocus.setBackgroundResource(R.drawable.btn_main_on_focus)
        mFocus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_focus_on, 0, 0)
    }

    private fun changeFragment(id: Int) {
        when(id) {
            1 -> {
                fragment = ConditionsFragment()
                changeColor(1)
            }
            2 -> {
                fragment = BreathsFragment()
                changeColor(2)
            }
            3 -> {
                fragment = MeditationsFragment()
                changeColor(3)
            }
            4 -> {
                fragment = SettingsFragment()
                changeColor(4)
            }
        }
        fragmentAdd()
    }

    private fun fragmentAdd() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.fragment_main, fragment)
        ft.commit()
    }

    @SuppressLint("ResourceAsColor")
    private fun changeColor(c0: Int) {
        val white = ContextCompat.getColor(applicationContext, R.color.white)
        val grey = ContextCompat.getColor(applicationContext, R.color.grey_light)

        when(c0) {
            1 -> {
                mFocus.setTextColor(white)
                mBreath.setTextColor(grey)
                mMeditation.setTextColor(grey)
                mSettings.setTextColor(grey)
                mFocus.setBackgroundResource(R.drawable.btn_main_on_focus)
                mBreath.setBackgroundResource(R.drawable.btn_main_off)
                mMeditation.setBackgroundResource(R.drawable.btn_main_off)
                mSettings.setBackgroundResource(R.drawable.btn_main_off)
                mFocus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_focus_on, 0, 0)
                mBreath.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_breath_off, 0, 0)
                mMeditation.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_meditation_off, 0, 0)
                mSettings.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings_off, 0, 0)
            }
            2 -> {
                mFocus.setTextColor(grey)
                mBreath.setTextColor(white)
                mMeditation.setTextColor(grey)
                mSettings.setTextColor(grey)
                mFocus.setBackgroundResource(R.drawable.btn_main_off)
                mBreath.setBackgroundResource(R.drawable.btn_main_on_beath)
                mMeditation.setBackgroundResource(R.drawable.btn_main_off)
                mSettings.setBackgroundResource(R.drawable.btn_main_off)
                mFocus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_focus_off, 0, 0)
                mBreath.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_breath_on, 0, 0)
                mMeditation.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_meditation_off, 0, 0)
                mSettings.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings_off, 0, 0)
            }
            3 -> {
                mFocus.setTextColor(grey)
                mBreath.setTextColor(grey)
                mMeditation.setTextColor(white)
                mSettings.setTextColor(grey)
                mFocus.setBackgroundResource(R.drawable.btn_main_off)
                mBreath.setBackgroundResource(R.drawable.btn_main_off)
                mMeditation.setBackgroundResource(R.drawable.btn_main_on_meditation)
                mSettings.setBackgroundResource(R.drawable.btn_main_off)
                mFocus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_focus_off, 0, 0)
                mBreath.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_breath_off, 0, 0)
                mMeditation.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_meditation_on, 0, 0)
                mSettings.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings_off, 0, 0)
            }
            4 -> {
                mFocus.setTextColor(grey)
                mBreath.setTextColor(grey)
                mMeditation.setTextColor(grey)
                mSettings.setTextColor(white)
                mFocus.setBackgroundResource(R.drawable.btn_main_off)
                mBreath.setBackgroundResource(R.drawable.btn_main_off)
                mMeditation.setBackgroundResource(R.drawable.btn_main_off)
                mSettings.setBackgroundResource(R.drawable.btn_main_on_settings)
                mFocus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_focus_off, 0, 0)
                mBreath.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_breath_off, 0, 0)
                mMeditation.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_meditation_off, 0, 0)
                mSettings.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings_on, 0, 0)
            }
        }
    }

    companion object MainObject {
        var fragmentFlag = 0

        const val TAG = "livetag"
        const val AUDIOFILE_BACKGROUND_TAG = "audioBreathTag"
        const val MAIN_TAG = "main_tag"
        const val FOCUS_TAG = "focus_tag"
        const val BREATH_TAG = "breath_tag"
        const val MEDITATION_TAG = "meditation_tag"
        const val SCREENDIALOG_TITLE_TAG = "screendialog_title_tag"
        const val SCREENDIALOG_BODY_TAG = "screendialog_body_tag"


        fun log(text: String) {
            Log.d(TAG, text)
        }

    }
}