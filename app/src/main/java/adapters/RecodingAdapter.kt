package adapters

import android.app.Activity
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alarm.app.alarmkotlin.CustomDialogBox
import com.alarm.app.alarmkotlin.R
import models.model
import kotlin.collections.ArrayList

class RecodingAdapter(var context: Activity,
                      var list: ArrayList<model>,
                      var toolbar: androidx.appcompat.widget.Toolbar,
                      var cdd : CustomDialogBox?)
    : RecyclerView.Adapter<RecodingAdapter.AlarmViewHolder>() {

    var listSelected = ArrayList<Boolean>()
    var isLongPressed : Boolean = false
    var selectAll = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.audio_list_item,parent,false)
        return AlarmViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(list == null)
            return 0
        else
            return list.size
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.tvName.text = list[position].name

//        if(position == list.size-1)
            setAnimation(holder.itemView)

        holder.itemView.setOnClickListener {

            if(!isLongPressed) {
                cdd = CustomDialogBox(
                    context,
                    Environment.getExternalStorageDirectory().path + "/MyRecorder/" + list[position].name,
                    list[position].name
                )
                Log.d("TAG", Environment.getExternalStorageDirectory().path + "/MyRecorder/" + list[position].name)
                cdd!!.show()
            }else {

                onLongPressed(position)
                notifyItemChanged(position)

            }
        }


        holder.itemView.setOnLongClickListener {

            if(!isLongPressed) {
                isLongPressed = true;
                listSelected.add(true)
                list[position].isSelected = true
            }else {
                onLongPressed(position)
            }
            notifyItemChanged(position)
            true
        }

        if(isLongPressed) {
            toolbar.visibility = View.VISIBLE
        }else {
            toolbar.visibility = View.INVISIBLE
        }


        if(list[position].isSelected){
            holder.view.visibility = View.VISIBLE
        }else {
            holder.view.visibility = View.GONE
        }
    }

    private fun setAnimation(viewToAnimate: View) {
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        viewToAnimate.startAnimation(animation)
    }


    fun onLongPressed(position: Int) {
        if(!list[position].isSelected) {
            list[position].isSelected = true
            listSelected.add(true)
            if(listSelected.size == list.size)
                selectAll = true
            Log.d("TAG",""+listSelected.size)
        } else {
            list[position].isSelected = false
            listSelected.removeAt(listSelected.size-1)
            Log.d("TAG",""+listSelected.size)
            selectAll = false
            if(listSelected.isEmpty())
                isLongPressed = false
        }
    }

    class AlarmViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var tvName : TextView;
        var view : View

        init {
            tvName = itemView.findViewById(R.id.tvName)
            view = itemView.findViewById(R.id.VSelected)
        }
    }



}