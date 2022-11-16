package ru.pankratov.trofimov.liveandhealth.adapters

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.models.MeditExerModelList
import ru.pankratov.trofimov.liveandhealth.models.PremiumExerModelList

class PremiumExersiceAdapter(
    private var premiumslist: ArrayList<PremiumExerModelList>,
    private val ctx: Context
    ) : RecyclerView
.Adapter<PremiumExersiceAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View, ctx: Context) : RecyclerView.ViewHolder(itemView) {

        val context = ctx

        fun bindExersice(list: PremiumExerModelList) {
            // зачеркнутый шрифт
            val styledText = Html.fromHtml("<s>" + list.priceOld + "</s>")

            titleView.text = list.title
            discountView.text = list.discount
            priceView.text = list.price
            priceOldView.text = styledText
            discriptionView.text = list.discription

            // + фирменный шрифт
            val fontAppZagolovok = Typeface.createFromAsset(context.assets, "Comfortaa-Bold.ttf")
            titleView.typeface = fontAppZagolovok
            discountView.typeface = fontAppZagolovok
            priceView.typeface = fontAppZagolovok
            priceOldView.typeface = fontAppZagolovok
            discriptionView.typeface = fontAppZagolovok

        }

        val titleView: TextView = itemView.findViewById(R.id.head_exemple_premium)
        val discountView: TextView = itemView.findViewById(R.id.discount_exemple_premium)
        val priceView: TextView = itemView.findViewById(R.id.price_exemple_premium)
        val priceOldView: TextView = itemView.findViewById(R.id.priceOld_exemple_premium)
        val discriptionView: TextView = itemView.findViewById(R.id.discription_exemple_premium)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.exemple_list_premium, parent, false)
        return MyViewHolder(itemView, ctx)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindExersice(premiumslist[position])

        holder.itemView.setOnClickListener {
//            holder.linear.setBackgroundResource(R.drawable.gradient_for_list_exersice_off)
//            val intent = Intent(ctx, MeditationActivity::class.java)
//            intent.putExtra(MainActivity.MEDITATION_TAG, position)
//            ctx.startActivity(intent)
        }
    }


    override fun getItemCount() = premiumslist.size

}