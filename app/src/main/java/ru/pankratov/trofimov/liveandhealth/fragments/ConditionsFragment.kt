package ru.pankratov.trofimov.liveandhealth.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.log
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.adapters.ConditionExersiceAdapter
import ru.pankratov.trofimov.liveandhealth.models.CondExerModelList


class ConditionsFragment : Fragment() {

    private lateinit var mTextHead: TextView
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

        return inflatedView
    }

    private fun addCategories() {
        for (i in 0..3) {
            val value = CondExerModelList()
            value.image = DRAWABLES_ICONS_FOCUS[i]
            value.imageCard = DRAWABLES_IMG_FOCUS[i]
            value.title = NAMES_FOCUS[i]
            value.discription = DISCRIPTIONS_FOCUS[i]
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
        val DISCRIPTIONS_FOCUS = arrayOf(
            "Имидж Искусство Благотворительность",
            "Семья Секс Духовность",
            "Финансы Работа Здоровье",
            "Знания Любовь Дружба",
        )
        val NAMES_FOCUS = arrayOf(
            "ОГОНЬ",
            "ВОДА",
            "ЗЕМЛЯ",
            "ВОЗДУХ",
        )

    }
}