package com.alarm.app.alarmkotlin

import services.RecordAudioServices
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log

fun Activity.startService(path: String,number : Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        val intent = Intent(this, RecordAudioServices::class.java)
        intent.putExtra("PATH",path)
        intent.putExtra("NUMBER",number)
        startService(intent)
    } else {
        val intent = Intent(this, RecordAudioServices::class.java)
        intent.putExtra("PATH",path)
        intent.putExtra("NUMBER",number)
        startService(intent)
    }
}

fun Activity.stopRecordingService() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        stopService(Intent(this, RecordAudioServices::class.java))
    } else {
        val intent = Intent(this, RecordAudioServices::class.java)
        stopService(intent)
//        unbindService(serviceConnection)
    }
}

val serviceConnection = object : ServiceConnection {
    override fun onServiceConnected(className: ComponentName, service: IBinder) {
        Log.d("TAG1","Connected")
    }

    override fun onServiceDisconnected(className: ComponentName) {
        Log.d("TAG1","Disconnected")
    }
}
