package ru.pankratov.trofimov.liveandhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_TAG

class EyesActivity : AppCompatActivity() {

    private lateinit var mText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eyes)

        mText = findViewById(R.id.textView_eyes)

        //получаем интент
        val intent = intent
        val indexMeditation = intent.getIntExtra(WORKOUT_TAG, 0)

        mText.text = "Было выбранно упражнение: $indexMeditation"

    }
}