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


        for (i in 0..10) {
            val value = CondExerModelList()
            value.image = R.mipmap.ic_btn_pause
            value.title = "title $i"
            value.discription = "discription"
            conditionslist.add(value)
        }
        log("list: $conditionslist")

        // адаптер
        val ctx = context
        mAdapterConditions = ctx?.let { ConditionExersiceAdapter(conditionslist, it) }
        mListConditionsView.layoutManager = LinearLayoutManager(context)
        mListConditionsView.adapter = mAdapterConditions

        return inflatedView
    }

    companion object ConditionObjects {

    }
}