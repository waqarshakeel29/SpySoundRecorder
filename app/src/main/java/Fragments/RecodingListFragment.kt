package Fragments


import adapters.RecodingAdapter
import android.opengl.Visibility
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alarm.app.alarmkotlin.CustomDialogBox

import com.alarm.app.alarmkotlin.R
import extension.delete
import extension.selectAll
import extension.shareSelected
import extension.unselectAll
import kotlinx.android.synthetic.main.fragment_recoding_list.*
import models.model
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RecodingListFragment : Fragment() {

    var cdd: CustomDialogBox? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recoding_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setRecyclerView()
    }

    companion object{
        var list = ArrayList<model>()
    }

    fun setRecyclerView(){


        if(list.isEmpty()){
            createList()
        }

        rvAudioList.layoutManager = GridLayoutManager(activity!!,2) as GridLayoutManager
        rvAudioList.adapter = RecodingAdapter(activity!!,list,toolbarMain,cdd)

        share.setOnClickListener {
            rvAudioList.shareSelected()
        }

        delete.setOnClickListener {
            rvAudioList.delete()
            list.clear()
            createList()
            rvAudioList.adapter!!.notifyDataSetChanged()
            (rvAudioList.adapter as RecodingAdapter).isLongPressed = false
            toolbarMain.visibility = View.INVISIBLE
        }

        selectAll.setOnClickListener {
            if(!(rvAudioList.adapter as RecodingAdapter).selectAll) {
                rvAudioList.selectAll()
                (rvAudioList.adapter as RecodingAdapter).selectAll = true
            }else{
                rvAudioList.unselectAll()
                toolbarMain.visibility = View.INVISIBLE
                (rvAudioList.adapter as RecodingAdapter).isLongPressed = false
                (rvAudioList.adapter as RecodingAdapter).selectAll = false
            }
            rvAudioList.adapter!!.notifyDataSetChanged()
        }
    }


    open fun createList() {

        if(!list.isNullOrEmpty())
            list.clear()

        var file = File(Environment.getExternalStorageDirectory().path + "/MyRecorder")

        if(file.exists()) {
            if (file.listFiles().isNotEmpty()) {

                for(i in file.listFiles()){
                    list.add(model(i.name,false))
                }
                list.sortDescending() //because we have used reversed recycler view
            } else{
                rvAudioList.visibility = View.GONE
                IvPlaceHolder.visibility = View.VISIBLE
            }
        }else{
            rvAudioList.visibility = View.GONE
            IvPlaceHolder.visibility = View.VISIBLE
        }
    }


    override fun onDestroy() {

        if(cdd != null) {
            cdd!!.dismiss()
        }

        super.onDestroy()
    }
}
