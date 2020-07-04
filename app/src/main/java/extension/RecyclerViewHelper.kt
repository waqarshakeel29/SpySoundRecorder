package extension

import Fragments.RecodingListFragment
import adapters.RecodingAdapter
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.alarm.app.alarmkotlin.MainActivity
import kotlinx.android.synthetic.main.fragment_recoding_list.view.*
import models.model
import java.io.File

fun RecyclerView.delete()
{
    var str = Environment.getExternalStorageDirectory().path + "/MyRecorder/"
//    for(i in (rvAudioList.adapter as RecodingAdapter).list){

    for(i in RecodingListFragment.list){
        if(i.isSelected) {
            File(str + i.name).delete()
        }
    }
}

fun RecyclerView.selectAll() {
    for(i in (rvAudioList.adapter as RecodingAdapter).list){
        if(!i.isSelected) {
            i.isSelected = true
        }
    }
}

fun RecyclerView.unselectAll() {
    for(i in (rvAudioList.adapter as RecodingAdapter).list){
        if(i.isSelected) {
            i.isSelected = false
        }
    }
}

fun RecyclerView.shareSelected()
{
    val urisList: ArrayList<Uri> = arrayListOf()
    var str = Environment.getExternalStorageDirectory().path + "/MyRecorder/"
    for(i in (rvAudioList.adapter as RecodingAdapter).list){
        if(i.isSelected)
            urisList.add(Uri.parse(str + i.name))
    }


    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND_MULTIPLE
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, urisList)
        type = "audio/*"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share File to..."))

}