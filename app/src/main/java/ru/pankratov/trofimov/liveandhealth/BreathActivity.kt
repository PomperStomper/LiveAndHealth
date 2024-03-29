package ru.pankratov.trofimov.liveandhealth

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.WindowManager
import com.budiyev.android.circularprogressbar.CircularProgressBar
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.SCREENDIALOG_BODY_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.SCREENDIALOG_TITLE_TAG
import ru.pankratov.trofimov.liveandhealth.controls.CircularRotateAnimation
import ru.pankratov.trofimov.liveandhealth.dialogs.ScreenDialog
import android.widget.*
import pl.droidsonroids.gif.GifImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.BREATH_TAG
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.app.Activity
import android.content.Intent
import android.graphics.Insets
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.AUDIOFILE_BACKGROUND_TAG
import ru.pankratov.trofimov.liveandhealth.dialogs.StartBreathDialog
import ru.pankratov.trofimov.liveandhealth.services.AudioService

class BreathActivity : AppCompatActivity() {

    private lateinit var mTextNameBreath: TextView
    private lateinit var mTextBreath: TextView
    private lateinit var mTextBreathNumber: TextView
    private lateinit var mNumberInhale: TextView
    private lateinit var mNumberDelay1: TextView
    private lateinit var mNumberExhalation: TextView
    private lateinit var mNumberDelay2: TextView

    private lateinit var mSlider: ImageView
    private lateinit var mRastish: GifImageView
    private lateinit var mTotalTime: TextView
    private lateinit var mCurrentTime: TextView

    private lateinit var progressBar: CircularProgressBar

    private lateinit var mBtnStart: Button
    private var seekBar: SeekBar? = null
    private var countDownTimer: CountDownTimer? = null
    private var animationSlider: CircularRotateAnimation? = null


    var inhale = false
    var exhalation = false

    // Эти 5 параметров получаем из списков для каждого упражнения
    var quantity = 0                     // кол-во повторений
    var TIME_INHALE: Long = 0            // время вдоха
    var TIME_DELAY_1: Long = 0           // время 1й задержки
    var TIME_EXHALATION: Long = 0        // время выдоха
    var TIME_DELAY_2: Long = 0           // время 2й задежки



    var TIME_BREATH: Long = 0            // время одного цикла
    var TIME_EXERCISE: Long = 0          // общее время упражнения
    var TIME_PASSED = 0                  // сколько прошло от общего времени

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_breath)
        // прозрачный статус бар
        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                countDownTimer?.cancel()
                stopAll()
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)


        //получаем интент (номер упражнения)
        val intent = intent
        val index = intent.getIntExtra(BREATH_TAG, 0)

        // получаем данные из списков
        val title = getTitleBreath(index)

        quantity = TIMES_QUANTITY[index]
        TIME_INHALE = TIMES_INHALE[index]
        TIME_DELAY_1 = TIMES_DELAY_1[index]
        TIME_EXHALATION = TIMES_EXHALATION[index]
        TIME_DELAY_2 = TIMES_DELAY_2[index]
        TIME_BREATH = TIME_INHALE + TIME_DELAY_1 + TIME_EXHALATION + TIME_DELAY_2 // время одного цикла
        TIME_EXERCISE = TIME_BREATH * quantity    // общее время упражнения

        mTextNameBreath = findViewById(R.id.text_name_exercise_breath)
        mTextBreath = findViewById(R.id.text_breath)
        mTextBreathNumber = findViewById(R.id.text_breath_number)
        mNumberInhale = findViewById(R.id.number_inhale_breath)
        mNumberDelay1 = findViewById(R.id.number_delay_1_breath)
        mNumberExhalation = findViewById(R.id.number_exhalation_breath)
        mNumberDelay2 = findViewById(R.id.number_delay_2_breath)
        mBtnStart = findViewById(R.id.button_start_breath)
        seekBar = findViewById(R.id.seekBar_breath)
        mTotalTime = findViewById(R.id.totalTime_breath)
        mCurrentTime = findViewById(R.id.currentTime_breath)
        mSlider = findViewById(R.id.slider_breath)
        mRastish = findViewById(R.id.rastish_breath)
        progressBar = findViewById(R.id.progress_bar)


        //устанавливаем начальное время упражнения
        mTotalTime.text = getTimeString(TIME_EXERCISE)
        // устанавливанем название
        mTextNameBreath.text = title
        // показываем интервалы
        mNumberInhale.text = (TIME_INHALE / 1000).toString() + "с."
        mNumberDelay1.text = (TIME_DELAY_1 / 1000).toString() + "с."
        mNumberExhalation.text = (TIME_EXHALATION / 1000).toString() + "с."
        mNumberDelay2.text = (TIME_DELAY_2 / 1000).toString() + "с."
        // скрываем бегунок
        mSlider.visibility = View.GONE
        // + фирменный шрифт
        val fontApp = Typeface.createFromAsset(assets, "Comfortaa-Bold.ttf")
        mTextNameBreath.typeface = fontApp
        mTextBreath.typeface = fontApp
        mTextBreathNumber.typeface = fontApp

        //устанавливаем максимальное значение
        seekBar!!.max = TIME_EXERCISE.toInt()

        // получаем ширину бегунка
        val widthSlider = getSliderWidth()
        // получаем ширину прогрессбара
        val widthProgressBar = getProgressWidth()

        // устанавливаем ширину прогрессбара
        val params: ViewGroup.LayoutParams = progressBar.layoutParams
        params.width = widthProgressBar
        progressBar.layoutParams = params

        // Добавляем анимацию вращения
        animationSlider = CircularRotateAnimation(mSlider, widthSlider)
        animationSlider?.duration = TIME_BREATH
        animationSlider?.repeatCount = TIME_EXERCISE.toInt() / 1000

        mBtnStart.setOnClickListener {
            startDialog()
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
    // рассчитываем ширину бегунка
    private fun getSliderWidth(): Float {
        val width = getScreenWidth(this)
        val t = (width.toFloat() / 2)
        val y = (t / 100) * 20
        return t - y
    }
    // рассчитываем ширину прогрессбара
    private fun getProgressWidth(): Int {
        val width = getScreenWidth(this)
        val a = (width / 100) *  17
        return width - a
    }

    // получаем название
    private fun getTitleBreath(index: Int): String? {
        val listTitle = resources.getStringArray(R.array.list_breath_exercise_array)
        return listTitle[index]
    }

    // обновление ПрогрессБар
    private val mHandler = Handler()
    private val mRunnable: Runnable = object : Runnable {
        override fun run() {
            //set max value
            progressBar.maximum = 100F
            progressBar.progressAnimationDuration = TIME_BREATH
            //set progress to current position
            val mPosition = animationSlider!!.progress
            progressBar.progress = mPosition
            //repeat above code every second
            mHandler.postDelayed(this, 10)
        }
    }

    fun start() {
        startExerciseTimer()
        startAudio()
        mSlider.startAnimation(animationSlider)
        mSlider.visibility = View.VISIBLE
        mBtnStart.isClickable = false
        mBtnStart.isActivated = true
        mRunnable.run()
    }
    // изменяем надпись в центре экрана в зависимости от фазы цикла
    private fun treatmentBreath(interval: Int, counter: Int) {
        when (interval) {
            0 -> {
                mTextBreath.text = ""
                mTextBreathNumber.text = ""
            }
            1 -> {
                mTextBreath.text = getString(R.string.text_inhale)
                if (inhale) mRastish.animate().scaleX(2.7f).scaleY(2.7f).duration = TIME_INHALE
                inhale = false
                mTextBreathNumber.text = ((TIME_BREATH / 1000) - (TIME_DELAY_2 / 1000) - (TIME_EXHALATION / 1000) - (TIME_DELAY_1 / 1000) - counter).toString()
            }
            2 -> {
                mTextBreath.text = getString(R.string.text_delay)
                mTextBreathNumber.text = ((TIME_BREATH / 1000) - (TIME_DELAY_2 / 1000) - (TIME_EXHALATION / 1000) - counter).toString()
            }
            3 -> {
                mTextBreath.text = getString(R.string.text_exhalation)
                if (exhalation) mRastish.animate().scaleX(1f).scaleY(1f).duration = TIME_EXHALATION
                exhalation =false
                mTextBreathNumber.text = ((TIME_BREATH / 1000) - (TIME_DELAY_2 / 1000) - counter).toString()
            }
            4 -> {
                mTextBreath.text = getString(R.string.text_delay)
                mTextBreathNumber.text = ((TIME_BREATH / 1000) - counter).toString()
            }
        }


    }
    // проверяем фазы цикла
    private fun intervalBreath(c0: Int): Int {
        var counter = 0
        if (c0 < TIME_INHALE / 1000) counter = 1
        if (c0 >= TIME_INHALE / 1000)  counter = 2
        if (c0 >= TIME_INHALE / 1000 + TIME_DELAY_1 / 1000)  counter = 3
        if (c0 >= TIME_INHALE / 1000 + TIME_DELAY_1 / 1000 + TIME_EXHALATION / 1000)  counter = 4
        return counter
    }
    // отсчет таймера
    private fun startExerciseTimer() {
        var counterInterval = 0
        countDownTimer = object : CountDownTimer(TIME_EXERCISE, 1000) {
            override fun onTick(millis: Long) {
                // обновляем время на общих часах и положение бегунка
                seekBarAndTimeExerciseUpdate()
                // находим интервал
                val interval = intervalBreath(counterInterval)
                // обрабатываем действия на экране
                treatmentBreath(interval, counterInterval)

                counterInterval += 1
                val mil = millis / 1000
//                log("seconds: $mil")
                TIME_PASSED += 1000

                if (counterInterval == (TIME_BREATH / 1000).toInt()) {
                    counterInterval = 0
                    inhale = true
                    exhalation = true
                }

            }
            override fun onFinish() {
                stopAll()
                screenDialog("Цикл закончен", "Конец")
            }
        }
        (countDownTimer as CountDownTimer).start()
        inhale = true
        exhalation = true
    }
    // изменения сикбара
    private fun seekBarAndTimeExerciseUpdate() {
        // обновляем время оставшееся
        val duration = TIME_EXERCISE - TIME_PASSED
        mTotalTime.text = getTimeString(duration)
        //обновляем позицию
        val mCurrentPosition = TIME_PASSED
        seekBar!!.progress = mCurrentPosition
        //обновляем время прошедшее
        mCurrentTime.text = getTimeString(mCurrentPosition.toLong())
    }

    private fun stopAll() {
        mTextBreath.text = ""
        mTextBreathNumber.text = ""
        // останавливаем анимацию
        mSlider.clearAnimation()
        animationSlider?.cancel()
        animationSlider?.reset()
        mRastish.clearAnimation()

        stopAudio()
        seekBarAndTimeExerciseUpdate()

        mSlider.visibility = View.GONE
    }

    private fun screenDialog(title: String, body: String) {
        try {
            val bundle = Bundle()
            bundle.putString(SCREENDIALOG_TITLE_TAG, title)
            bundle.putString(SCREENDIALOG_BODY_TAG, body)
            val dialog = ScreenDialog()
            dialog.arguments = bundle
            val manager = supportFragmentManager
            dialog.show(manager, "ScreenDialog")
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun startDialog() {
        try {
            val dialog = StartBreathDialog()
            val manager = supportFragmentManager
            dialog.show(manager, "StartDialog")
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    // правильное отображение времени
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
//        Log.d("123","время: $buf")
        return buf.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        stopAll()
    }

    override fun onPause() {
        super.onPause()
        stopAudio()
    }

    override fun onResume() {
        super.onResume()
//        startAudio()
    }

    private fun startAudio() {
        val intent = Intent(this, AudioService::class.java)
        intent.putExtra(AUDIOFILE_BACKGROUND_TAG, AUDIO_BACKGROUND)
        startService(intent)
    }

    private fun stopAudio() {
        stopService(Intent(this, AudioService::class.java))
    }


    companion object Breath {
        const val AUDIO_BACKGROUND = R.raw.backgroundbreath
        val TIMES_INHALE = arrayListOf<Long>(
            3000, 3000, 3000, 3000, 9000, 5000, 4000, 600, 4000, 5000, 600, 9000, 4000, 4000, 20000
        )
        val TIMES_DELAY_1 = arrayListOf<Long>(
            0, 0, 6000, 6000, 0, 0, 2000, 0, 0, 15000, 0, 0, 0, 4000, 20000
        )
        val TIMES_EXHALATION = arrayListOf<Long>(
            9000, 6000, 3000, 6000, 2000, 5000, 1000, 400, 8000, 5000, 400, 6000, 8000, 4000, 20000
        )
        val TIMES_DELAY_2 = arrayListOf<Long>(
            0, 6000, 6000, 0, 0, 5000, 5000, 0, 12000, 2500, 0, 0, 0, 4000, 0
        )
        val TIMES_QUANTITY = arrayListOf(
            35, 28, 24, 32, 28, 40, 5, 420, 18, 18, 60, 28, 35, 27, 7
        )
    }

}