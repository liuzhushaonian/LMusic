package com.example.legend.lmusic.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.legend.lmusic.controller.PlayHelper;

public class NoisyReceiver extends BroadcastReceiver {

    private static final String action="android.media.AUDIO_BECOMING_NOISY";
    private PlayHelper playHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        playHelper=PlayHelper.getPlayHelper();

        if(intent.getAction().equals(action)){
            playHelper.pause();
            Toast.makeText(context,"耳机已拔出",Toast.LENGTH_SHORT).show();
        }

    }
}
