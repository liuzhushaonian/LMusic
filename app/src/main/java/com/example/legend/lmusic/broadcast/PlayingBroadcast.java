package com.example.legend.lmusic.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.legend.lmusic.controller.PlayHelper;
import com.example.legend.lmusic.view.NotificationHelper;

public class PlayingBroadcast extends BroadcastReceiver {


    private final static int PLAY=0x1;
    private final static int PREVIOUS=0x2;
    private final static int NEXT=0x3;
    private final static int PAUSE=0x4;
    private PlayHelper playHelper;


    public PlayingBroadcast() {
       playHelper=PlayHelper.getPlayHelper();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//
        String action=intent.getAction();
        switch (action){
            case NotificationHelper.PLAY:
                playHelper.continues();

                break;
            case NotificationHelper.PAUSE:
                playHelper.pause();

                break;
            case NotificationHelper.PREVIOUS:

                playHelper.previous();

                break;
            case NotificationHelper.NEXT:
                playHelper.next(PlayHelper.UI);

                break;
        }
    }
}

