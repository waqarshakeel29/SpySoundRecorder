package com.alarm.app.alarmkotlin

import android.R.attr.start
import android.content.Context
import android.media.AudioFormat
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.io.IOException
import java.util.concurrent.Semaphore


class AudioRecorder(path:String) {

    var recorder = MediaRecorder()
    var path:String
    var number : Int = 0

    init{
        this.path = sanitizePath(path)
        Log.d("TAG",this.path)
    }

    private fun sanitizePath(path:String):String {
        var path = path
        if (!path.startsWith("/"))
        {
            path = "/$path"
        }
        if (!path.contains("."))
        {
            path += ".mp3"
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath() + path
    }


    fun start() {
            recorder = MediaRecorder()
            val state = android.os.Environment.getExternalStorageState()
            if (state != android.os.Environment.MEDIA_MOUNTED) {
                throw IOException("SD Card is not mounted.  It is " + state + ".")
            }

            // make sure the directory we plan to store the recording in exists
            val directory = File(path).getParentFile()
            if (!directory.exists() && !directory.mkdirs()) {
                throw IOException("Path to file could not be created.")
            }

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder.setAudioChannels(AudioFormat.CHANNEL_CONFIGURATION_MONO)
            recorder.setOutputFile(path)
            recorder.prepare()
            recorder.start()
    }


    fun stop() : String? {

            try {
                recorder.stop()
                recorder.release()
                return this.path
            } catch (stopException: RuntimeException) {
                delete()
                return null
            }
    }

    fun delete(){
        File(path).delete()
    }

    fun playarcoding(path:String) {
        val mp = MediaPlayer()
        mp.setDataSource(path)
        mp.prepare()
        mp.start()
        mp.setVolume(10f, 10f)
    }
}