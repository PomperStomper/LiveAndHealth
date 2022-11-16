package ru.pankratov.trofimov.liveandhealth.fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.fragmentFlag
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.adapters.MeditationExersiceAdapter
import ru.pankratov.trofimov.liveandhealth.models.MeditExerModelList

class MeditationsFragment : Fragment() {

    private lateinit var mTextHead: TextView
    private lateinit var mBtnAbout: ImageView
    private lateinit var mListMeditationsView: RecyclerView

    private var meditationslist = arrayListOf<MeditExerModelList>()
    var mAdapterMeditations: MeditationExersiceAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_meditations, container, false)

        mTextHead = inflatedView.findViewById(R.id.textView_head_meditation)
        mBtnAbout = inflatedView.findViewById(R.id.image_about_meditation)
        mListMeditationsView = inflatedView.findViewById(R.id.listView_meditations)

        mTextHead.text = getString(R.string.meditations_text)
        // + фирменный шрифт
        val fontAppZagolovok = Typeface.createFromAsset(resources.assets, "Comfortaa-Bold.ttf")
        mTextHead.typeface = fontAppZagolovok

        // заполняем список медитаций
        getListMeditation()

        // адаптер
        val ctx = context
        mAdapterMeditations = ctx?.let { MeditationExersiceAdapter(meditationslist, it) }
        mListMeditationsView.layoutManager = LinearLayoutManager(context)
        mListMeditationsView.isNestedScrollingEnabled = false
        mListMeditationsView.setHasFixedSize(true)
        mListMeditationsView.adapter = mAdapterMeditations

        mBtnAbout.setOnClickListener {
            startAboutFragment()
        }

        return inflatedView
    }

    private fun startAboutFragment() {
        fragmentFlag = 3
        val fragment: Fragment = AboutFragment()
        val fm = activity?.supportFragmentManager
        val ft = fm?.beginTransaction()
        ft?.replace(R.id.fragment_main, fragment)
        ft?.commit()
    }

    private fun getListMeditation() {
        val listName = resources.getStringArray(R.array.list_meditation_exercise_array)
        val listDiscr = resources.getStringArray(R.array.list_meditation_description_array)

        for (i in listName.indices) {
            val value = MeditExerModelList()
            value.image = ICONS_MEDITATIONS[i]
            value.title = listName[i]
            value.discription = listDiscr[i]
            meditationslist.add(value)
        }
    }

    companion object {
        val ICONS_MEDITATIONS = arrayOf(
            R.mipmap.ic_meditation_maze,
            R.mipmap.ic_meditation_fiery_force,
            R.mipmap.ic_meditation_abyss_of_silence,
            R.mipmap.ic_meditation_reality_show
        )
    }

}