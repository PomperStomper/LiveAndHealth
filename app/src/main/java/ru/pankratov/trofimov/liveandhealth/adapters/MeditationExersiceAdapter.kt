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
import ru.pankratov.trofimov.liveandhealth.MainActivity
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.MEDITATION_TAG
import ru.pankratov.trofimov.liveandhealth.MeditationActivity
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.models.MeditExerModelList

class MeditationExersiceAdapter(private var meditationslist: ArrayList<MeditExerModelList>, private val ctx: Context) : RecyclerView
.Adapter<MeditationExersiceAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View, ctx: Context) : RecyclerView.ViewHolder(itemView) {

        val context = ctx

        fun bindExersice(list: MeditExerModelList) {
            list.image?.let { imageView.setImageResource(it) }
            titleView.text = list.title
            discView.text = list.discription
            linear.setBackgroundResource(R.drawable.gradient_for_list_exersice_meditation)

            // + фирменный шрифт
            val fontAppZagolovok = Typeface.createFromAsset(context.assets, "Comfortaa-Bold.ttf")
            titleView.typeface = fontAppZagolovok
            discView.typeface = fontAppZagolovok

            discView.visibility = View.GONE

            btnFullDiscView.setOnClickListener {
                when (BreathExersiceAdapter.full) {
                    true -> {
                        discView.visibility = View.GONE
                        BreathExersiceAdapter.full = false
                        btnFullDiscView.setImageResource(R.drawable.btn_down)
                    }
                    false -> {
                        discView.visibility = View.VISIBLE
                        BreathExersiceAdapter.full = true
                        btnFullDiscView.setImageResource(R.drawable.btn_up)
                    }
                }
            }
        }

        val imageView: ImageView = itemView.findViewById(R.id.imageView_exersice_view)
        val titleView: TextView = itemView.findViewById(R.id.title_exersice_view)
        val discView: TextView = itemView.findViewById(R.id.desc_exersice_view)
        val btnFullDiscView: ImageView = itemView.findViewById(R.id.recycler_list_full_btn)
        val linear: LinearLayout = itemView.findViewById(R.id.linear_exemple_exersice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.exemple_list_exersice, parent, false)
        return MyViewHolder(itemView, ctx)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindExersice(meditationslist[position])

        holder.itemView.setOnClickListener {
//            holder.linear.setBackgroundResource(R.drawable.gradient_for_list_exersice_off)
            val intent = Intent(ctx, MeditationActivity::class.java)
            intent.putExtra(MEDITATION_TAG, position)
            ctx.startActivity(intent)
        }
    }


    override fun getItemCount() = meditationslist.size

    companion object {
        var full = false
    }
}