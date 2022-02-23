package ru.pankratov.trofimov.liveandhealth.dialogs

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import ru.pankratov.trofimov.liveandhealth.BreathActivity
import ru.pankratov.trofimov.liveandhealth.MeditationActivity
import ru.pankratov.trofimov.liveandhealth.R

class StartMeditationDialog: DialogFragment() {

    var timer: CountDownTimer? = null
    var time: Long = 3

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
        val view: View = inflater.inflate(R.layout.start_meditation_dialog_layout, container)
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)


        val textTitle = view.findViewById<View>(R.id.text_startdialog_title) as TextView
        val textBody = view.findViewById<View>(R.id.text_startdialog_body) as TextView
        val textNumber = view.findViewById<View>(R.id.text_startdialog_number) as TextView
        // + фирменный шрифт
        val fontApp = Typeface.createFromAsset(context?.assets, "Comfortaa-Bold.ttf")
        textTitle.typeface = fontApp
        textBody.typeface = fontApp

        (activity as MeditationActivity).startAudio()
        startExerciseTimer(textNumber)

        return view
    }

    // отсчет таймера
    private fun startExerciseTimer(textNumber: TextView) {
        timer = object : CountDownTimer(TIME_START, 1000) {
            override fun onTick(millis: Long) {
                time = millis / 1000
                textNumber.text = time.toString()
            }
            override fun onFinish() {
                dismiss()
                (activity as MeditationActivity).stopAudio()
                (activity as MeditationActivity).play()
            }
        }
        (timer as CountDownTimer).start()
    }

    companion object {
        val TIME_START: Long = 26000
    }
}