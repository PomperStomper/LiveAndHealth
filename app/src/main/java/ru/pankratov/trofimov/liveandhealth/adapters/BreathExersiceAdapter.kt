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
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.BREATH_TAG
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.models.BreathExerModelList

class BreathExersiceAdapter(private var conditionslist: List<BreathExerModelList>, private val ctx: Context) : RecyclerView
.Adapter<BreathExersiceAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View, ctx: Context) : RecyclerView.ViewHolder(itemView) {

        val context = ctx

        fun bindExersice(list: BreathExerModelList) {
            list.image?.let { imageView.setImageResource(it) }
            titleView.text = list.title
            DiscView.text = list.discription
            linear.setBackgroundResource(R.drawable.gradient_for_list_exersice_breath)

            // + фирменный шрифт
            val fontAppZagolovok = Typeface.createFromAsset(context.assets, "Comfortaa-Bold.ttf")
            titleView.typeface = fontAppZagolovok
            DiscView.typeface = fontAppZagolovok
        }

        val imageView: ImageView = itemView.findViewById(R.id.imageView_exersice_view)
        val titleView: TextView = itemView.findViewById(R.id.title_exersice_view)
        val DiscView: TextView = itemView.findViewById(R.id.desc_exersice_view)

        val linear: LinearLayout = itemView.findViewById(R.id.linear_exemple_exersice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.exemple_list_exersice, parent, false)
        return MyViewHolder(itemView, ctx)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindExersice(conditionslist[position])

        holder.itemView.setOnClickListener {
            holder.linear.setBackgroundResource(R.drawable.gradient_for_list_exersice_off)
            val intent = Intent(ctx, BreathActivity::class.java)
            intent.putExtra(BREATH_TAG, position)
            ctx.startActivity(intent)
        }
    }

    override fun getItemCount() = conditionslist.size
}