package ru.pankratov.trofimov.liveandhealth.dialogs

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.SCREENDIALOG_BODY_TAG
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.SCREENDIALOG_TITLE_TAG
import ru.pankratov.trofimov.liveandhealth.R

class ScreenDialog : DialogFragment() {

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.screen_dialog_layout, container)
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        val args = arguments
        val title: String = if (args?.getString(SCREENDIALOG_TITLE_TAG) != null) args.getString(SCREENDIALOG_TITLE_TAG)!!
        else getString(R.string.app_name)
        val body: String = if (args?.getString(SCREENDIALOG_BODY_TAG) != null) args.getString(SCREENDIALOG_BODY_TAG)!!
        else getString(R.string.text_chtotoposhlonetak)

        val textTitle = view.findViewById<View>(R.id.text_screendialog_title) as TextView
        val textBody = view.findViewById<View>(R.id.text_screendilog_body) as TextView
        val layout = view.findViewById<View>(R.id.layout_screen_dialog) as CardView
        // + фирменный шрифт
        val fontApp = Typeface.createFromAsset(context?.assets, "Comfortaa-Bold.ttf")
        textTitle.typeface = fontApp
        textBody.typeface = fontApp

        textTitle.text = title
        textBody.text = body

        layout.setOnClickListener {
            dismiss()
        }
        return view
    }
}