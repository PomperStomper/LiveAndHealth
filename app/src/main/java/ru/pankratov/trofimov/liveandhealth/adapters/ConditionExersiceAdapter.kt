package ru.pankratov.trofimov.liveandhealth.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.pankratov.trofimov.liveandhealth.BreathActivity
import ru.pankratov.trofimov.liveandhealth.ConditionActivity
import ru.pankratov.trofimov.liveandhealth.MainActivity
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.models.CondExerModelList

class ConditionExersiceAdapter(private var conditionslist: List<CondExerModelList>, private val ctx: Context) : RecyclerView
.Adapter<ConditionExersiceAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View, ctx: Context) : RecyclerView.ViewHolder(itemView) {

        val context = ctx

        fun bindExersice(list: CondExerModelList) {
            list.image?.let { imageView.setImageResource(it) }
            list.imageCard?.let { imageViewCard.setImageResource(it) }
            titleView.text = list.title
            DiscView.text = list.discription

            // + фирменный шрифт
            val fontAppZagolovok = Typeface.createFromAsset(context.assets, "Comfortaa-Bold.ttf")
            titleView.typeface = fontAppZagolovok
            DiscView.typeface = fontAppZagolovok
        }

        val imageView: ImageView = itemView.findViewById(R.id.imageView_title_categories)
        val imageViewCard: ImageView = itemView.findViewById(R.id.imageView_categories)
        val titleView: TextView = itemView.findViewById(R.id.title_view_categories)
        val DiscView: TextView = itemView.findViewById(R.id.desc_view_categories)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.exemple_list_categories, parent, false)
        return MyViewHolder(itemView, ctx)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindExersice(conditionslist[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(ctx, ConditionActivity::class.java)
            intent.putExtra(MainActivity.WORKOUT_TAG, position)
            ctx.startActivity(intent)
        }
    }


    override fun getItemCount() = conditionslist.size
}