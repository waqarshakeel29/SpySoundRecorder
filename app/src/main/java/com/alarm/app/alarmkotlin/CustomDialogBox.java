package com.alarm.app.alarmkotlin;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class CustomDialogBox extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    String audioName;
    SeekBar seekBar;
    ImageView imageView;
    TextView tvTotal;
    TextView tvCurrent;
    TextView Name;


    Handler mHandler;
    MediaPlayer mediaPlayer;

    public CustomDialogBox(Activity a, String path, String name) {
        super(a);
        this.c = a;
        setAudioPath(path);
        audioName = name;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        init();

        mHandler = new Handler();
        c.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);

                    int minutes = mCurrentPosition / 60;
                    int hours = minutes /60;
                    mCurrentPosition = mCurrentPosition % 60;
                    minutes = minutes % 60;
                    hours = hours % 60;
                    tvCurrent.setText(String.format("%02d", hours)+ ":" + String.format("%02d", minutes) + ":"+ String.format("%02d", mCurrentPosition));

//                    tvCurrent.setText(secondToTimeString(mCurrentPosition));
                }
                mHandler.postDelayed(this, 1000);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void init() {
        seekBar = findViewById(R.id.SbSeekbar);
        imageView = findViewById(R.id.ImagePlayPause);
        Name = findViewById(R.id.TvAudioName);
        tvCurrent = findViewById(R.id.TvCurrent);
        tvTotal = findViewById(R.id.TvTotal);
//        cardView = findViewById(R.id.cardView);

        seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        imageView.setOnClickListener(this);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
//        setHeightAndWidthOfDialog(height,width);


        Name.setText(audioName);
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        int seconds = mediaPlayer.getDuration() / 1000;
//        tvTotal.setText(secondToTimeString(seconds));

        int minutes = seconds / 60;
        int hours = minutes /60;
        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;
        tvTotal.setText(String.format("%02d", hours)+ ":" + String.format("%02d", minutes) + ":"+ String.format("%02d", seconds));

    }

    void setAudioPath(String path) {
        mediaPlayer = MediaPlayer.create(c,
                Uri.parse(path));
    }

    @Override
    public void onClick(View v) {


        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            imageView.setImageResource(R.drawable.pause_button);
        } else {
            mediaPlayer.pause();
            imageView.setImageResource(R.drawable.play_button);
        }
    }

    private String secondToTimeString(int seconds) {

        int min = seconds / 60;
        int hours = min / 60;

        if (hours < 1) {
            return zeroString(min) + ":" + zeroString(seconds % 60);
        } else {
            return zeroString(hours) + ":" + zeroString(min) + ":" + zeroString(seconds % 60);
        }
    }

    private String zeroString(int val) {
        if (val / 10 < 1) {
            return "0" + val;
        } else
            return "" + val;
    }

    @Override
    public void dismiss() {
        super.dismiss();

        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }




    //    public void setHeightAndWidthOfDialog(int height,int width) {
//        ViewGroup.LayoutParams l = cardView.getLayoutParams();
//        l.width = (int)(width * 0.9);
//        cardView.setLayoutParams(l);
//
//    }

}