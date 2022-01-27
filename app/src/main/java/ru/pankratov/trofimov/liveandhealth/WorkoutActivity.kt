package ru.pankratov.trofimov.liveandhealth

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.MAIN_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_TAG
import java.io.*

class WorkoutActivity : AppCompatActivity() {

    private lateinit var mImageWourkout: ImageView
    private lateinit var mTextZagolovok: TextView
    private lateinit var mListWourkout: ListView

    private var indexActivity: Int = 0

//    private var workoutlist = arrayListOf<ListWorkout>()
//    var mAdapter: ListWorkoutAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)
        //получаем интент
        val intent = intent
        indexActivity = intent.getIntExtra(MAIN_TAG, 0)
        // находим объекты
        mImageWourkout = findViewById(R.id.image_workout)
        mListWourkout = findViewById(R.id.listView_workout)
        mTextZagolovok = findViewById(R.id.textView_head_meditation)



        // вставляем картинку и заголовок согласно интенту
//        mImageWourkout.setImageResource(DRAWABLE_LIST_IMAGE_MAIN[indexActivity])
        // + фирменный шрифт
        val fontAppZagolovok = Typeface.createFromAsset(assets, "Comfortaa-Bold.ttf")
        mTextZagolovok.typeface = fontAppZagolovok
        mTextZagolovok.text = ""

        // прокрутка списка
        val n = 0 // прокручиваем до начала
        mListWourkout.smoothScrollToPosition(n)
        // адаптер
//        mAdapter = ListWorkoutAdapter(workoutlist, this)
//        mListWourkout.adapter = mAdapter
//        // нажатие на список
//        mListWourkout.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
//            playActivity(indexActivity, i)
//        }

        // добавляем в список картинки и названия
//        inputExerciseInList()
    }

    // Заполняем Списки упражнений и описаний из ресурсов
//    private fun inputExerciseInList() {
//        when (indexActivity) {
//            0 -> {
//                val listName = resources.getStringArray(R.array.list_eyes_exercise_array)
//                val listDescription = resources.getStringArray(R.array.list_eyes_description_array)
//                addWorkoutList(listName, listDescription)
//            }
//            1 -> {
//                val listName = resources.getStringArray(R.array.list_breath_exercise_array)
//                val listDescription = resources.getStringArray(R.array.list_breath_description_array)
//                addWorkoutList(listName, listDescription)
//            }
//            2 -> {
//                val listName = resources.getStringArray(R.array.list_meditation_exercise_array)
//                val listDescription = resources.getStringArray(R.array.list_meditation_description_array)
//                addWorkoutList(listName, listDescription)
//            }
//        }
//    }
//    private fun addWorkoutList(listName: Array<String>, listDescription: Array<String>) {
//        for (i in listName.indices) {
//            val value = ListWorkout()
//            value.image = drawableListIcon[indexActivity]
//            value.title = listName[i]
//            value.discription = listDescription[i]
//            workoutlist.add(value)
//        }
//    }
    // выбранное упражнение
    private fun playActivity(indexActivity: Int, i: Int) {
        when (indexActivity) {
            0 -> {
                val intent = Intent(this, BreathActivity::class.java)
                intent.putExtra(WORKOUT_TAG ,i)
                startActivity(intent)
            }
            1 -> {
                val intent = Intent(this, BreathActivity::class.java)
                intent.putExtra(WORKOUT_TAG ,i)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, MeditationActivity::class.java)
                intent.putExtra(WORKOUT_TAG ,i)
                startActivity(intent)
            }
        }
    }

    companion object {
        // список иконок к категориям
        val drawableListIcon = arrayListOf(
            R.mipmap.ic_eyes,
            R.mipmap.ic_breath,
            R.mipmap.ic_meditation
        )
    }

}