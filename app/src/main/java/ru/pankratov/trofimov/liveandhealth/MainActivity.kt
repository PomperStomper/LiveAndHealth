package ru.pankratov.trofimov.liveandhealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import ru.pankratov.trofimov.liveandhealth.fragments.HomeFragment
import ru.pankratov.trofimov.liveandhealth.fragments.SettingsFragment
import ru.pankratov.trofimov.liveandhealth.fragments.StatisticsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()

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

    fun playActivity(c0: Int) {
        val intent = Intent(this, WorkoutActivity::class.java)
        intent.putExtra(MAIN_TAG ,c0)
        startActivity(intent)
    }

    companion object MainObject {
        const val TAG = "live_tag"
        const val MAIN_TAG = "main_tag"
        const val WORKOUT_TAG = "workout_tag"

        fun log(text: String) {
            Log.d(TAG, text)
        }

        val DRAWABLE_LIST_IMAGE_MAIN = arrayListOf(
            R.drawable.eyes,
            R.drawable.breath,
            R.drawable.meditation
        )
    }
}