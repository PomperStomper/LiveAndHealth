package ru.pankratov.trofimov.liveandhealth.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.fragmentFlag
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.log
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.adapters.ConditionExersiceAdapter
import ru.pankratov.trofimov.liveandhealth.models.CondExerModelList


class ConditionsFragment : Fragment() {

    private lateinit var mTextHead: TextView
    private lateinit var mBtnAbout: ImageView
    private lateinit var mListConditionsView: RecyclerView

    private var conditionslist = arrayListOf<CondExerModelList>()
    var mAdapterConditions: ConditionExersiceAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_conditions, container, false)

        mTextHead = inflatedView.findViewById(R.id.textView_head_condition)
        mBtnAbout = inflatedView.findViewById(R.id.image_about_condition)
        mTextHead.text = getString(R.string.conditions_text)
        // + фирменный шрифт
        val fontAppZagolovok = Typeface.createFromAsset(resources.assets, "Comfortaa-Bold.ttf")
        mTextHead.typeface = fontAppZagolovok

        mListConditionsView = inflatedView.findViewById(R.id.listView_conditions)

        addCategories()

        // адаптер
        val ctx = context
        mAdapterConditions = ctx?.let { ConditionExersiceAdapter(conditionslist, it) }
        mListConditionsView.layoutManager = LinearLayoutManager(context)
        mListConditionsView.adapter = mAdapterConditions

        mBtnAbout.setOnClickListener {
            startAboutFragment()
        }

        return inflatedView
    }

    private fun startAboutFragment() {
        fragmentFlag = 1
        val fragment: Fragment = AboutFragment()
        val fm = activity?.supportFragmentManager
        val ft = fm?.beginTransaction()
        ft?.replace(R.id.fragment_main, fragment)
        ft?.commit()
    }

    private fun addCategories() {
        val listNames = resources.getStringArray(R.array.list_focus_exercise_array)
        val listDiscriptions = resources.getStringArray(R.array.list_focus_description_array)

        for (i in 0..3) {
            val value = CondExerModelList()
            value.image = DRAWABLES_ICONS_FOCUS[i]
            value.imageCard = DRAWABLES_IMG_FOCUS[i]
            value.title = listNames[i]
            value.discription = listDiscriptions[i]
            conditionslist.add(value)
        }
        log("list: $conditionslist")
    }

    companion object ConditionObjects {
        val DRAWABLES_ICONS_FOCUS = arrayOf(
            R.drawable.fireimg,
            R.drawable.waterimg,
            R.drawable.earthimg,
            R.drawable.airimg,
        )
        val DRAWABLES_IMG_FOCUS = arrayOf(
            R.drawable.fire2img,
            R.drawable.water2img,
            R.drawable.earth2img,
            R.drawable.air2img,
        )
    }
}