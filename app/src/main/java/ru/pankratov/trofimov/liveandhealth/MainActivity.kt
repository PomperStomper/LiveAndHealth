package ru.pankratov.trofimov.liveandhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import ru.pankratov.trofimov.liveandhealth.adapters.ListMainAdapter
import ru.pankratov.trofimov.liveandhealth.fragments.HomeFragment
import ru.pankratov.trofimov.liveandhealth.fragments.SettingsFragment
import ru.pankratov.trofimov.liveandhealth.fragments.StatisticsFragment
import ru.pankratov.trofimov.liveandhealth.models.ListMain

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startFirstFragment()
    }

    private fun startFirstFragment() {
        val fragment: Fragment = HomeFragment()
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.fragment_main, fragment)
        ft.commit()
    }

    fun changeFragment(view: View) {

        val fragment: Fragment = when(view.id){
            R.id.btn_home_main -> HomeFragment()
            R.id.btn_statistics_main -> StatisticsFragment()
            R.id.btn_settings_main -> SettingsFragment()
            else -> HomeFragment()
        }
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.fragment_main, fragment)
        ft.commit()

    }

    companion object MainObject {
        const val TAG = "livetag"

        fun log(text: String) {
            Log.d(TAG, text)
        }
    }
}