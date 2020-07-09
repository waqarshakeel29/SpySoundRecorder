package com.alarm.app.alarmkotlin

import Fragments.RecodingFragment
import Fragments.RecodingListFragment
import adapters.RecodingAdapter
import adapters.SectionStatePagerAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Placeholder
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.tramsun.libs.prefcompat.Pref
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_recoding_list.*
import kotlinx.android.synthetic.main.fragment_recoding_list.view.*
import org.w3c.dom.Text
import androidx.viewpager.widget.ViewPager
import android.content.Context.MODE_PRIVATE

class MainActivity : AppCompatActivity() {

    var cdd : CustomDialogBox? = null
    var ListSize : Int = 0
    var isPermissionGranted = false
    lateinit var sectionStatePagerAdapter : SectionStatePagerAdapter

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Pref.init(this)
        requestPermission()





    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("ListSize",RecodingListFragment.list.size)
        Log.d("TAG5","Save = " + RecodingListFragment.list.size)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        ListSize = savedInstanceState.getInt("ListSize")
    }

    fun openFragments(){
        sectionStatePagerAdapter = SectionStatePagerAdapter(supportFragmentManager)

        sectionStatePagerAdapter.addFragment(RecodingFragment(), "Recorder")
        sectionStatePagerAdapter.addFragment(RecodingListFragment(), "Recording List")

        vp_main.adapter = sectionStatePagerAdapter

        tabLayout.setupWithViewPager(vp_main)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab) {
            }

            override fun onTabSelected(p0: TabLayout.Tab) {
                if (p0.position == 1) {
                    if (ListSize != RecodingListFragment.list.size) {
                        vp_main.rvAudioList.adapter!!.notifyDataSetChanged()
                        ListSize = RecodingListFragment.list.size
                    }

                    Log.d("TAG5","" + ListSize)
                    if (ListSize == 1 && IvPlaceHolder.visibility == View.VISIBLE) {
                        Log.d("TAG5","OK"+"In")
                        (sectionStatePagerAdapter.getItem(1) as RecodingListFragment).createList()
                        vp_main.rvAudioList.layoutManager = GridLayoutManager(this@MainActivity, 2)
                        vp_main.rvAudioList.adapter =
                            RecodingAdapter(this@MainActivity, RecodingListFragment.list, toolbarMain,cdd)
                        IvPlaceHolder.visibility = View.GONE
                        vp_main.rvAudioList.visibility = View.VISIBLE
                    }
                }

                vp_main.currentItem = p0.position
            }
        })
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                ), 123
            )
        } else {

        }
    }

    fun permission(requestCode: Int, grantResults: IntArray) {
        if (requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            openFragments()
        } else {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("Permission")
            dialog.setMessage("Permissions are required for recording. Please press OK to allow or Cancel to Close App.")
            dialog.setPositiveButton("OK"){dialogInterface, i ->
                requestPermission()
            }.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> dialogInterface.dismiss()
                finish()
            }
                .setCancelable(false)
                .show()
        }
    }

//    var mydir = context.getDir("mydir", Context.MODE_PRIVATE) //Creating an internal dir;
//    var fileWithinMyDir = File(mydir, "myfile") //Getting a file within the dir.

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permission(requestCode,grantResults)
    }

    override fun onDestroy() {
        if(cdd != null){
            cdd!!.dismiss()
        }
        super.onDestroy()
    }
}

