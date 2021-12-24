package ru.pankratov.trofimov.liveandhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class BreathActivity : AppCompatActivity() {

    private lateinit var mText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breath)

        mText = findViewById(R.id.textView_breath)

        //получаем интент
        val intent = intent
        val indexMeditation = intent.getIntExtra(MainActivity.WORKOUT_TAG, 0)

        mText.text = "Было выбранно упражнение: $indexMeditation"
    }
}