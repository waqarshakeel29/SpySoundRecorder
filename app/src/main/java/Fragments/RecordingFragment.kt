package Fragments


import android.annotation.SuppressLint
import android.content.*
import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alarm.app.alarmkotlin.MainActivity

import com.alarm.app.alarmkotlin.R
import kotlinx.android.synthetic.main.fragment_recording.*
import com.alarm.app.alarmkotlin.startService
import com.alarm.app.alarmkotlin.stopRecordingService
import com.tramsun.libs.prefcompat.Pref
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_recoding_list.*
import kotlinx.android.synthetic.main.save_file.*
import models.model
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RecodingFragment : Fragment() {

    lateinit var vibe : Vibrator
    var fileName = "REC-"
    var number = 0
    var modelNumber = 0

    open val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            Log.d("TAG1","RECIVING")
            time.setText(intent.getStringExtra("TIME"))
            modelNumber = intent.getIntExtra("NUMBER",0)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recording, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vibe = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        var file = File(Environment.getExternalStorageDirectory().path + "/MyRecorder")
//        var file = File(Environment.getExternalStorageDirectory().path)
        file.mkdir()
//        Toast.makeText(activity!!,"Path: " + ContextCompat.getExternalFilesDirs(activity!!, null)[0].path,Toast.LENGTH_LONG).show()
//        Log.d("SecondReleaseError","Path: " + ContextCompat.getExternalFilesDirs(activity!!, null)[0].path)
        Log.d("SecondReleaseError","Path: " + file.listFiles())
        if(file.listFiles().isNotEmpty()){
            var listOfFiles = file.listFiles()
            listOfFiles.sort()
            if(listOfFiles[0].name.contains("REC")) {
                number = listOfFiles[listOfFiles.size-1].name
                    .substring(listOfFiles[0].name.indexOf('-')+1,listOfFiles[0].name.indexOf('.')).toInt()
            }else{
                number = 0
            }
        }
        else{
            number = 0
        }

        fab.setOnClickListener{

            var isStarted = Pref.getBoolean("IsStarted",false)

            if (!isStarted) {

                LocalBroadcastManager.getInstance(activity!!)
                    .registerReceiver(mBroadcastReceiver, IntentFilter("AudioRecording"))

                Pref.putBoolean("IsStarted",true)

                val animation = AnimationUtils.loadAnimation(activity!!, R.anim.zoom_in)
                image.startAnimation(animation)


                number = number + 1
                vibe.vibrate(200)
                activity!!.startService("/MyRecorder/" + fileName + number.toString(),number)

            }else {
                LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(mBroadcastReceiver)

                image.clearAnimation()
                time.setText("START")
                activity!!.stopRecordingService()
                Pref.putBoolean("IsStarted",false)

                var str : String = fileName + modelNumber.toString()



                var dialog = AlertDialog.Builder(activity!!)
                    .setView(R.layout.save_file)
                    .setCancelable(false)
                    .show()


                var save = dialog.findViewById<Button>(R.id.save)
                var editText= dialog.findViewById<EditText>(R.id.et)
                var delete  = dialog.findViewById<Button>(R.id.delete)

                editText!!.setText(str)
                editText!!.selectAll()

                save!!.setOnClickListener {
                    if(editText!!.text.toString() != ""){
                        var firstElement = editText!!.text.toString()[0].toInt()
                        if(((firstElement > 65 && firstElement < 90) || (firstElement > 97 && firstElement < 122))) {
                            var path = Environment.getExternalStorageDirectory().path + "/MyRecorder/"
                            var src = File(path + str + ".mp3")
                            str = editText.text.toString()
                            src.renameTo(File(path + str + ".mp3"))
                            RecodingListFragment.list.add(model("$str.mp3",false))
                            dialog.dismiss()

                            (activity as MainActivity).vp_main.setCurrentItem(1)
                            (activity as MainActivity).rvAudioList.scrollToPosition((activity as MainActivity).ListSize-1)

                        }else{
                            Toast.makeText(activity!!,"Name must start with Letter",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(activity!!,"Enter Name",Toast.LENGTH_SHORT).show()
                    }
                }
                delete!!.setOnClickListener {
                    var path = Environment.getExternalStorageDirectory().path + "/MyRecorder/"
                    File(path + str + ".mp3").delete()
                    dialog.dismiss()
                    Toast.makeText(activity!!,"DELETED : " + str + ".mp3"
                        ,Toast.LENGTH_SHORT).show()
                    number--
                }
            }
        }
    }



    override fun onPause() {
        super.onPause()
            LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(mBroadcastReceiver)
    }


    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(activity!!)
            .registerReceiver(mBroadcastReceiver, IntentFilter("AudioRecording"))
    }
}
