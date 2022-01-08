package ru.pankratov.trofimov.liveandhealth.fragments

import android.content.res.AssetManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import ru.pankratov.trofimov.liveandhealth.MainActivity
import ru.pankratov.trofimov.liveandhealth.MainActivity.MainObject.DRAWABLE_LIST_IMAGE_MAIN
import ru.pankratov.trofimov.liveandhealth.R
import ru.pankratov.trofimov.liveandhealth.adapters.ListMainAdapter
import ru.pankratov.trofimov.liveandhealth.models.ListMain


class HomeFragment : Fragment() {

    private lateinit var mlistMain: ListView
    private var workoutFragmentlist = arrayListOf<ListMain>()

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
            value.image = DRAWABLE_LIST_IMAGE_MAIN[i]
            value.title = mMainListArray[i]
            value.discription = mMainListDiscArray[i]
            workoutFragmentlist.add(value)
        }

        mlistMain = inflatedView.findViewById(R.id.listViewMain)
        // прокрутка списка
        val n = 0 // прокручиваем до начала
        mlistMain.smoothScrollToPosition(n)
        // адаптер
        val cont = requireActivity().baseContext
        val asset = requireActivity().assets
        val mAdapter = ListMainAdapter(workoutFragmentlist, cont, asset)
        mlistMain.adapter = mAdapter

        // нажатие на список
        mlistMain.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            when (i) {
                0 -> (activity as MainActivity?)?.playActivity(0)
                1 -> (activity as MainActivity?)?.playActivity(1)
                2 -> (activity as MainActivity?)?.playActivity(2)
            }
        }

        return inflatedView
    }
}