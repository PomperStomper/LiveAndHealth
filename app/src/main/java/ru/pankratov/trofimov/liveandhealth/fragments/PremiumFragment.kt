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
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.adapters.PremiumExersiceAdapter
import ru.pankratov.trofimov.liveandhealth.models.PremiumExerModelList

class PremiumFragment : Fragment() {

    private lateinit var mTextHead: TextView
    private lateinit var mListPremiumsView: RecyclerView

    private var premiumslist = arrayListOf<PremiumExerModelList>()
    var mAdapterPremiums: PremiumExersiceAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_premium, container, false)

        mTextHead = inflatedView.findViewById(R.id.name_premium)
        mListPremiumsView = inflatedView.findViewById(R.id.listView_premiums)
        // + фирменный шрифт
        val fontAppZagolovok = Typeface.createFromAsset(resources.assets, "Comfortaa-Bold.ttf")
        mTextHead.typeface = fontAppZagolovok

        getListPremiums()

        // адаптер
        val ctx = context
        mAdapterPremiums = ctx?.let { PremiumExersiceAdapter(premiumslist, it) }
        mListPremiumsView.layoutManager = LinearLayoutManager(context)
        mListPremiumsView.isNestedScrollingEnabled = false
        mListPremiumsView.setHasFixedSize(true)
        mListPremiumsView.adapter = mAdapterPremiums

        return inflatedView
    }

    private fun getListPremiums() {
        val listTitles = resources.getStringArray(R.array.array_date_premium)
        val listPrices = resources.getStringArray(R.array.array_price_premium)
        val listDiscounts = resources.getStringArray(R.array.array_discount_premium)
        val listPriceOlds = resources.getStringArray(R.array.array_price_premium_old)
        val listDiscriptions = resources.getStringArray(R.array.array_discription_premium)

        for (i in listTitles.indices) {
            val value = PremiumExerModelList()
            value.title = listTitles[i]
            value.discount = listDiscounts[i]
            value.price = listPrices[i]
            value.priceOld = listPriceOlds[i]
            value.discription = listDiscriptions[i]
            premiumslist.add(value)
        }
    }

}