package ru.pankratov.trofimov.liveandhealth.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.models.ListMeditation

class ListWorkoutAdapter(items: ArrayList<ListMeditation>, ctx: Context) :
    ArrayAdapter<ListMeditation>(ctx, R.layout.list_workout_layout, items) {

    //view holder is used to prevent findViewById calls
    private class AttractionItemViewHolder {
        var image: ImageView? = null
        var title: TextView? = null
        var discription: TextView? = null
    }

    override fun getView(i: Int, vieww: View?, viewGroup: ViewGroup): View {

        var view = vieww
        val viewHolder: AttractionItemViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_workout_layout, viewGroup, false)

            viewHolder = AttractionItemViewHolder()
            viewHolder.image = view!!.findViewById<View>(R.id.imageView_list_workout) as ImageView
            viewHolder.title = view.findViewById<View>(R.id.textView_list_workout) as TextView
            viewHolder.discription = view.findViewById<View>(R.id.textView_description_workout) as TextView
        } else {
            viewHolder = view.tag as AttractionItemViewHolder
        }

        val attraction = getItem(i)
        attraction!!.image?.let { viewHolder.image!!.setImageResource(it) }
        viewHolder.title!!.text = attraction.title
        viewHolder.discription!!.text = attraction.discription

        view.tag = viewHolder
        return view
    }
}