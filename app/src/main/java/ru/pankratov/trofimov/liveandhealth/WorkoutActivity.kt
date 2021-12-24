package ru.pankratov.trofimov.liveandhealth

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.DRAWABLE_LIST_IMAGE_MAIN
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.MAIN_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_TAG
import ru.pankratov.trofimov.liveandhealth.adapters.ListWorkoutAdapter
import ru.pankratov.trofimov.liveandhealth.models.ListMeditation

class WorkoutActivity : AppCompatActivity() {

    private lateinit var mImageWourkout: ImageView
    private lateinit var mTextZagolovok: TextView
    private lateinit var mListMeditation: ListView
    private var workoutlist = arrayListOf<ListMeditation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)
        //получаем интент
        val intent = intent
        val index = intent.getIntExtra(MAIN_TAG, 0)
        // находим объекты
        mImageWourkout = findViewById(R.id.image_workout)
        mListMeditation = findViewById(R.id.listView_meditation)
        mTextZagolovok = findViewById(R.id.textView_head_meditation)

        // получаем список категорий
        val mMainListArray = resources.getStringArray(R.array.main_list_array)
        // вставляем картинку и заголовок согласно интенту
        mImageWourkout.setImageResource(DRAWABLE_LIST_IMAGE_MAIN[index])
        mTextZagolovok.text = mMainListArray[index]

        // добавляем в список картинки и названия
        for (i in 0..20) {
            val value = ListMeditation()
            value.image = R.drawable.ic_melody
            value.title = "Упражнение $i"
            value.discription = "Краткое описание"
            workoutlist.add(value)
        }

        // прокрутка списка
        val n = 0 // прокручиваем до начала
        mListMeditation.smoothScrollToPosition(n)
        // адаптер
        val mAdapter = ListWorkoutAdapter(workoutlist, this)
        mListMeditation.adapter = mAdapter
        // нажатие на список
        mListMeditation.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            playActivity(index, i)
        }


    }

    private fun playActivity(c0: Int, c1: Int) {
        when (c0) {
            0 -> {
                val intent = Intent(this, EyesActivity::class.java)
                intent.putExtra(WORKOUT_TAG ,c1)
                startActivity(intent)
            }
            1 -> {
                val intent = Intent(this, BreathActivity::class.java)
                intent.putExtra(WORKOUT_TAG ,c1)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, MeditationActivity::class.java)
                intent.putExtra(WORKOUT_TAG ,c1)
                startActivity(intent)
            }
        }
    }
}