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
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_LINK_AUDIO_MEDITATION_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_LINK_IMAGE_MEDITATION_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_NAME_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.WORKOUT_TAG
import ru.pankratov.trofimov.liveandhealth.adapters.ListWorkoutAdapter
import ru.pankratov.trofimov.liveandhealth.models.ListMeditation

class WorkoutActivity : AppCompatActivity() {

    private lateinit var mImageWourkout: ImageView
    private lateinit var mTextZagolovok: TextView
    private lateinit var mListWourkout: ListView

    private var mMainListArray: Array<String>? = null
    private var workoutlist = arrayListOf<ListMeditation>()

    private var mListAudioMeditationArray: Array<String>? = null
    private var mListImageMeditationArray: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)
        //получаем интент
        val intent = intent
        val indexActivity = intent.getIntExtra(MAIN_TAG, 0)
        // находим объекты
        mImageWourkout = findViewById(R.id.image_workout)
        mListWourkout = findViewById(R.id.listView_meditation)
        mTextZagolovok = findViewById(R.id.textView_head_meditation)

        // получаем список категорий
        mMainListArray = resources.getStringArray(R.array.main_list_array)
        // если:
        //      глаза - то пока ничего
        //      дыхание - пока ничего
        //      медитации - заполняем списки ссылок на аудио и картинки
        when (indexActivity) {
            0 -> ""
            1 -> ""
            2 -> {
                mListAudioMeditationArray = resources.getStringArray(R.array.list_meditation_audio_array)
                mListImageMeditationArray = resources.getStringArray(R.array.list_meditation_image_array)
            }
        }

        // вставляем картинку и заголовок согласно интенту
        mImageWourkout.setImageResource(DRAWABLE_LIST_IMAGE_MAIN[indexActivity])
        mTextZagolovok.text = mMainListArray!![indexActivity]

        // добавляем в список картинки и названия
        inputExerciseInList(indexActivity)

        // прокрутка списка
        val n = 0 // прокручиваем до начала
        mListWourkout.smoothScrollToPosition(n)
        // адаптер
        val mAdapter = ListWorkoutAdapter(workoutlist, this)
        mListWourkout.adapter = mAdapter
        // нажатие на список
        mListWourkout.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            mListAudioMeditationArray?.get(i)
                ?.let { mListImageMeditationArray?.get(i)
                    ?.let { it1 -> playActivity(indexActivity, i, it, it1) } }
        }
    }

    private fun inputExerciseInList(c0: Int) {
        when (c0) {
            0 -> {
                val listName = resources.getStringArray(R.array.list_eyes_exercise_array)
                val listDescription = resources.getStringArray(R.array.list_eyes_description_array)
                addWorkoutList(listName, listDescription)
            }
            1 -> {
                val listName = resources.getStringArray(R.array.list_breath_exercise_array)
                val listDescription = resources.getStringArray(R.array.list_breath_description_array)
                addWorkoutList(listName, listDescription)
            }
            2 -> {
                val listName = resources.getStringArray(R.array.list_meditation_exercise_array)
                val listDescription = resources.getStringArray(R.array.list_meditation_description_array)
                addWorkoutList(listName, listDescription)
            }
        }
    }
    private fun addWorkoutList(listName: Array<String>, listDescription: Array<String>) {
        for (i in listName.indices) {
            val value = ListMeditation()
            value.image = R.drawable.ic_melody
            value.title = listName[i]
            value.discription = listDescription[i]
            workoutlist.add(value)
        }
    }

    private fun playActivity(indexActivity: Int, i: Int, linkAudioMeditation: String, linkImageMeditation: String) {
        when (indexActivity) {
            0 -> {
                val intent = Intent(this, EyesActivity::class.java)
                // пока ничего не отправляем
                startActivity(intent)
            }
            1 -> {
                val intent = Intent(this, BreathActivity::class.java)
                // пока ничего не отправляем
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, MeditationActivity::class.java)
                intent.putExtra(WORKOUT_TAG ,i)
                intent.putExtra(WORKOUT_NAME_TAG ,mMainListArray!![i])
                intent.putExtra(WORKOUT_LINK_AUDIO_MEDITATION_TAG ,linkAudioMeditation)
                intent.putExtra(WORKOUT_LINK_IMAGE_MEDITATION_TAG ,linkImageMeditation)
                startActivity(intent)
            }
        }
    }
}