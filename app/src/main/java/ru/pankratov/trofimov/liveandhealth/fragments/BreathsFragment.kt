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
import ru.pankratov.trofimov.liveandhealth.MainActivity
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.models.BreathExerModelList
import ru.pankratov.trofimov.liveandhealth.adapters.BreathExersiceAdapter


class BreathsFragment : Fragment() {

    private lateinit var mTextHead: TextView
    private lateinit var mListBreathsView: RecyclerView

    private var breathslist = arrayListOf<BreathExerModelList>()
    var mAdapterBreaths: BreathExersiceAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_breaths, container, false)

        mTextHead = inflatedView.findViewById(R.id.textView_head_breath)
        mListBreathsView = inflatedView.findViewById(R.id.listView_breaths)

        for (i in 10..28) {
            val value = BreathExerModelList()
            value.image = R.mipmap.ic_btn_pause
            value.title = "title $i"
            value.discription = "discription"
            breathslist.add(value)
        }
        MainActivity.log("list: $breathslist")

        mTextHead.text = "Дыхание"
        // + фирменный шрифт
        val fontAppZagolovok = Typeface.createFromAsset(resources.assets, "Comfortaa-Bold.ttf")
        mTextHead.typeface = fontAppZagolovok

        // адаптер
        val ctx = context
        mAdapterBreaths = ctx?.let { BreathExersiceAdapter(breathslist, it) }
        mListBreathsView.layoutManager = LinearLayoutManager(context)
        mListBreathsView.isNestedScrollingEnabled = false
        mListBreathsView.setHasFixedSize(true)
        mListBreathsView.adapter = mAdapterBreaths



        return inflatedView
    }
}