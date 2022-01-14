package ru.pankratov.trofimov.liveandhealth

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import com.budiyev.android.circularprogressbar.CircularProgressBar
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.SCREENDIALOG_BODY_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.SCREENDIALOG_TITLE_TAG
import ru.pankratov.trofimov.liveandhealth.controls.CircularRotateAnimation
import ru.pankratov.trofimov.liveandhealth.dialogs.ScreenDialog
import android.os.SystemClock
import android.widget.*
import pl.droidsonroids.gif.GifImageView


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

    // Эти 5 параметров необходимо получать из базы данных для каждого упражнения
    val quantity = 20                        // кол-во повторений
    val TIME_INHALE: Long = 5000            // время вдоха
    val TIME_DELAY_1: Long = 3000           // время 1й задержки
    val TIME_EXHALATION: Long = 4000        // время выдоха
    val TIME_DELAY_2: Long = 2000           // время 2й задежки



    val TIME_BREATH: Long = TIME_INHALE + TIME_DELAY_1 + TIME_EXHALATION + TIME_DELAY_2 // время одного цикла
    val TIME_EXERCISE: Long = TIME_BREATH * quantity    // общее время упражнения
    var TIME_PASSED = 0                    // сколько прошло от общего времени

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_breath)
        //получаем интент
        val intent = intent
        val index = intent.getIntExtra(MainActivity.WORKOUT_TAG, 0)

        // дыхание - заполняем список ссылок
        val listName = resources.getStringArray(R.array.list_breath_exercise_array)
        val listLinkBreathArray = resources.getStringArray(R.array.list_meditation_audio_array)

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
        mTextNameBreath.text = listName[index]
        // показываем интервалы
        mNumberInhale.text = ": " + (TIME_INHALE / 1000).toString() + "с."
        mNumberDelay1.text = ": " + (TIME_DELAY_1 / 1000).toString() + "с."
        mNumberExhalation.text = ": " + (TIME_EXHALATION / 1000).toString() + "с."
        mNumberDelay2.text = ": " + (TIME_DELAY_2 / 1000).toString() + "с."
        // скрываем бегунок
        mSlider.visibility = View.GONE
        // + фирменный шрифт
        val fontApp = Typeface.createFromAsset(assets, "Comfortaa-Bold.ttf")
        mTextNameBreath.typeface = fontApp
        mTextBreath.typeface = fontApp
        mTextBreathNumber.typeface = fontApp

        // Добавляем анимацию вращения
        animationSlider = CircularRotateAnimation(mSlider, 400F, this)
        animationSlider?.duration = TIME_BREATH
        animationSlider?.repeatCount = TIME_EXERCISE.toInt() / 1000

        mBtnStart.setOnClickListener {
            start()
        }

//        screenDialog("Это диалог", "здесь будет текст сообщения")

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
        mSlider.startAnimation(animationSlider)
        mSlider.visibility = View.VISIBLE
        mBtnStart.isClickable = false
        mRunnable.run()
    }

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

    private fun intervalBreath(c0: Int): Int {
        var counter = 0
        if (c0 < TIME_INHALE / 1000) counter = 1
        if (c0 >= TIME_INHALE / 1000)  counter = 2
        if (c0 >= TIME_INHALE / 1000 + TIME_DELAY_1 / 1000)  counter = 3
        if (c0 >= TIME_INHALE / 1000 + TIME_DELAY_1 / 1000 + TIME_EXHALATION / 1000)  counter = 4
        return counter
    }

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
                TIME_PASSED += 1000

                if (counterInterval == (TIME_BREATH / 1000).toInt()) {
                    counterInterval = 0
                    inhale = true
                    exhalation = true
                }

            }
            override fun onFinish() {
                stopAll()
                screenDialog("Это диалог", "Конец")
            }
        }
        (countDownTimer as CountDownTimer).start()
        inhale = true
        exhalation = true
    }

    private fun seekBarAndTimeExerciseUpdate() {
        //устанавливаем максимальное значение
        seekBar!!.max = TIME_EXERCISE.toInt()
        // обновляем время оставшееся
        val mDuration = TIME_EXERCISE - TIME_PASSED
        mTotalTime.text = getTimeString(mDuration)
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
//        Log.d("123","время: $buf")
        return buf.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        stopAll()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        countDownTimer?.cancel()
        stopAll()
    }

}