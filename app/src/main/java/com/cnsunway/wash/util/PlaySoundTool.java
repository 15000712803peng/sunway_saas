package com.cnsunway.wash.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.cnsunway.wash.R;

public class PlaySoundTool {

    static SoundPool soundPool;
    static int dingdong;

    public static void play(Context context) {
        soundPool = new SoundPool(3, AudioManager.STREAM_ALARM, 100);
        dingdong = soundPool.load(context, R.raw.notification, 1);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                soundPool.play(dingdong, 1, 1, 0, 0, 1);
            }
        });

    }


    public static void play(Context context, int music) {
        soundPool = new SoundPool(3, AudioManager.STREAM_ALARM, 100);
        dingdong = soundPool.load(context, music, 1);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                soundPool.play(dingdong, 1, 1, 0, 0, 1);
            }
        });

    }

}
