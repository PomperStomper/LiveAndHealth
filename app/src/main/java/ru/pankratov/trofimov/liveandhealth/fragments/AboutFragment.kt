package ru.pankratov.trofimov.liveandhealth.fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.pankratov.trofimov.liveandhealth.R

class AboutFragment : Fragment() {

    private lateinit var mBtnBack: ImageView
    private lateinit var mTextName: TextView
    private lateinit var mTextDiscription: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflatedView = inflater.inflate(R.layout.fragment_about, container, false)

        // кнопка назад
        mBtnBack = inflatedView.findViewById(R.id.button_back_about)
        mTextName = inflatedView.findViewById(R.id.text_name_about)
        mTextDiscription = inflatedView.findViewById(R.id.text_disc_about)
        // + фирменный шрифт
        val fontAppZagolovok = Typeface.createFromAsset(context?.assets, "Comfortaa-Bold.ttf")
        mTextName.typeface = fontAppZagolovok
        mTextDiscription.typeface = fontAppZagolovok

        mBtnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        return inflatedView
    }
}