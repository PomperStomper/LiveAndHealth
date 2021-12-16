package ru.pankratov.trofimov.liveandhealth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.pankratov.trofimov.liveandhealth.MainActivity
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.adapters.ListMainAdapter
import ru.pankratov.trofimov.liveandhealth.models.ListMain


class HomeFragment : Fragment() {

    private lateinit var mlistMain: ListView
    private var workuotlist = arrayListOf<ListMain>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_home, container, false)

        // получаем список категорий
        val mMainListArray = resources.getStringArray(R.array.main_list_array)
        // получаем список описаний
        val mMainListDiscArray = resources.getStringArray(R.array.main_list_discription_array)

        // добавляем в список картинки и названия
        for (i in mMainListArray.indices) {
            val value = ListMain()
            value.image = drawableList[i]
            value.title = mMainListArray[i]
            value.discription = mMainListDiscArray[i]
            workuotlist.add(value)
        }

        mlistMain = inflatedView.findViewById(R.id.listViewMain)
        // прокрутка списка
        val n = 0 // прокручиваем до начала
        mlistMain.smoothScrollToPosition(n)
        // адаптер
        val cont = requireActivity().baseContext
        val mAdapter = ListMainAdapter(workuotlist, cont)
        mlistMain.adapter = mAdapter

        // нажатие на список
        mlistMain.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            Toast.makeText(context, "нажал $i", Toast.LENGTH_SHORT).show()
        }

        return inflatedView
    }

    companion object {
        val drawableList = arrayListOf(
            R.drawable.eyesimg,
            R.drawable.meditationimg,
            R.drawable.breathimg
        )
    }
}