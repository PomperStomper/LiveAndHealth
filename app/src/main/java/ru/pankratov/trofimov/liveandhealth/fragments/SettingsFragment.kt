package ru.pankratov.trofimov.liveandhealth.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.fragmentAboutFlag
import ru.pankratov.trofimov.liveandhealth.R

class SettingsFragment : Fragment() {

    private lateinit var mShare: TextView
    private lateinit var mReview: TextView
    private lateinit var mSendMail: TextView
    private lateinit var mAbout: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_settings, container, false)

        mShare = inflatedView.findViewById(R.id.text_share_settings)
        mReview = inflatedView.findViewById(R.id.text_review_settings)
        mSendMail = inflatedView.findViewById(R.id.text_send_settings)
        mAbout = inflatedView.findViewById(R.id.text_about_settings)

        mShare.setOnClickListener {
            share()
        }
        mReview.setOnClickListener {
            review()
        }
        mSendMail.setOnClickListener {
            sendMail()
        }
        mAbout.setOnClickListener {
            startAboutAppFragment()
        }

        return inflatedView
    }

    private fun share() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT, getString(R.string.app_name) + ", Ссылка" + " - " + getString(R.string.link_app)
        )
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, "Поделиться"))
    }

    private fun review() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(getString(R.string.link_app))
        startActivity(intent)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun sendMail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:avhelp2018@gmail.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Добрый день.\n")
        if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
            startActivity(intent)
        }
    }

    private fun startAboutAppFragment() {
        fragmentAboutFlag = true
        val fragment: Fragment = AboutAppFragment()
        val fm = activity?.supportFragmentManager
        val ft = fm?.beginTransaction()
        ft?.replace(R.id.fragment_main, fragment)
        ft?.commit()
    }
}