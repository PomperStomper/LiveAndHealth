package ru.pankratov.trofimov.liveandhealth

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import ru.pankratov.trofimov.liveandhealth.controls.CircularRotateAnimation

class BreathActivity : AppCompatActivity() {

    private lateinit var mTextNameBreath: TextView
    private lateinit var mTextBreath: TextView

    private lateinit var mSlider: ImageView
    private lateinit var mRastish: ImageView
    private lateinit var mTotalTime: TextView
    private lateinit var mCurrentTime: TextView

    private lateinit var mBtnStart: Button
    private var seekBar: SeekBar? = null
    private var countDownTimer: CountDownTimer? = null
    private var animationSlider: CircularRotateAnimation? = null

    var inhale = false
    var exhalation = false

    val quantity = 2
    val TIME_INHALE: Long = 5000
    val TIME_DELAY_1: Long = 3000
    val TIME_EXHALATION: Long = 4000
    val TIME_DELAY_2: Long = 2000
    val TIME_BREATH: Long = TIME_INHALE + TIME_DELAY_1 + TIME_EXHALATION + TIME_DELAY_2
    val TIME_EXERCISE: Long = TIME_BREATH * quantity
    var TIME_CURRENT = 0

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
        mBtnStart = findViewById(R.id.button_start_breath)
        seekBar = findViewById(R.id.seekBar_breath)
        mTotalTime = findViewById(R.id.totalTime_breath)
        mCurrentTime = findViewById(R.id.currentTime_breath)
        mSlider = findViewById(R.id.slider_breath)
        mRastish = findViewById(R.id.rastish_breath)

        //устанавливаем начальное время упражнения
        mTotalTime.text = getTimeString(TIME_EXERCISE)
        // устанавливанем название
        mTextNameBreath.text = listName[index]
        // скрываем бегунок
        mSlider.visibility = View.GONE

        // Добавляем анимацию
        animationSlider = CircularRotateAnimation(mSlider, 400F)
        animationSlider?.duration = TIME_BREATH
        animationSlider?.repeatCount = TIME_EXERCISE.toInt() / 1000

        mBtnStart.setOnClickListener {
            startExerciseTimer()
            mSlider.startAnimation(animationSlider)
            mSlider.visibility = View.VISIBLE
            mBtnStart.isClickable = false
        }


    }

    private fun startExerciseTimer() {
        var before = 0
        countDownTimer = object : CountDownTimer(TIME_EXERCISE, 1000) {
            @SuppressLint("ResourceAsColor")
            override fun onTick(millis: Long) {
                // обновляем время на общих часах и положение бегунка
                seekBarAndTimeExerciseUpdate()

                if (before < TIME_INHALE / 1000) {
                    mTextBreath.text = getString(R.string.text_inhale)
                    if (inhale) mRastish.animate().scaleX(2.7f).scaleY(2.7f).duration = TIME_INHALE
                    inhale = false
                }
                if (before >= TIME_INHALE / 1000) {
                    mTextBreath.text = getString(R.string.text_delay)
                }
                if (before >= TIME_INHALE / 1000 + TIME_DELAY_1 / 1000) {
                    mTextBreath.text = getString(R.string.text_exhalation)
                    if (exhalation) mRastish.animate().scaleX(1f).scaleY(1f).duration = TIME_EXHALATION
                    exhalation =false
                }
                if (before >= TIME_INHALE / 1000 + TIME_DELAY_1 / 1000 + TIME_EXHALATION / 1000) {
                    mTextBreath.text = getString(R.string.text_delay)
                }

                before += 1
                TIME_CURRENT += 1000
//                Log.d("123","прошло $before")

                if (before == (TIME_BREATH / 1000).toInt()) {
                    before = 0
                    inhale = true
                    exhalation = true
                }

            }
            override fun onFinish() {
                stopAll()
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
        val mDuration = TIME_EXERCISE - TIME_CURRENT
        mTotalTime.text = getTimeString(mDuration)
        //обновляем позицию
        val mCurrentPosition = TIME_CURRENT
        seekBar!!.progress = mCurrentPosition
        //обновляем время прошедшее
        mCurrentTime.text = getTimeString(mCurrentPosition.toLong())
    }

    private fun stopAll() {
        mTextBreath.text = ""
        // останавливаем анимацию
        mSlider.clearAnimation()
        animationSlider?.cancel()
        animationSlider?.reset()
        mRastish.clearAnimation()

        seekBarAndTimeExerciseUpdate()

        mSlider.visibility = View.GONE
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
        Log.d("123","время: $buf")
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