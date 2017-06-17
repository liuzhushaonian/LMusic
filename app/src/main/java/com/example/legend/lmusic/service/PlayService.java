package com.example.legend.lmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.Toast;

import com.example.legend.lmusic.controller.PlayControl;
import com.example.legend.lmusic.controller.PlayHelper;
import com.example.legend.lmusic.controller.PlayStatus;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Database;
import com.example.legend.lmusic.model.Mp3Info;
import com.example.legend.lmusic.view.ChangeUILinstner;
import com.example.legend.lmusic.view.NotificationHelper;
import com.example.legend.lmusic.view.Play;

import java.io.IOException;

public class PlayService extends Service implements PlayControl,AudioManager.OnAudioFocusChangeListener{

    private static PlayService playService;
    private MediaPlayer mediaPlayer;
    private ChangeUILinstner changeUILinstner;
    private Mp3Info mp3Info;
    private int position;
    private static final int PLAY=0x000001;
    private static final int PAUSE=0x000002;
    private static final int FALSE=0x000003;
    private Mp3Database mp3Database;
    private static final String HISTORYMUSIC="history";
    PlayHelper playHelper;
    private NotificationHelper notificationHelper;







    @Override
    public void play(final Mp3Info mp3Info) {
        this.mp3Info=mp3Info;
        if(mediaPlayer!=null){
        final int millisecond=1000;
            mediaPlayer.stop();
            mediaPlayer.reset();

            //开线程执行播放
        new Thread() {
            @Override
            public void run() {
                try {

                    mediaPlayer.setDataSource(LApplication.getContext(), Uri.parse(mp3Info.getUrl()));
                    mediaPlayer.setOnPreparedListener(preparedListener);
                    mediaPlayer.prepareAsync();
//                    mediaPlayer.start();

                    Message message=new Message();
                    message.what=PLAY;
                    handler.sendMessage(message);

                    saveHistory(mp3Info);
                } catch (IOException e) {

                    Message message=new Message();
                    message.what=FALSE;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }




            }
            }.start();
        }

    }

    MediaPlayer.OnPreparedListener preparedListener=new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            notificationHelper.playingNotification(mp3Info,true);
        }
    };


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PLAY:
                    changeUILinstner.changeInfo(mp3Info);
                    changeUILinstner.changePlay(Play.PLAY);
                    playHelper.setPlay(Play.PLAY);
                    break;
                case FALSE:
                    Toast.makeText(LApplication.getContext(),"该歌曲无法播放，可能已经被删除",Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;

            }

            super.handleMessage(msg);

        }
    };


    @Override
    public void pause() {
        if (mediaPlayer!=null){
            mediaPlayer.pause();
            changeUILinstner.changePlay(Play.PAUSE);
            notificationHelper.playingNotification(mp3Info,false);
            playHelper.setPlay(Play.PAUSE);
        }
    }

    @Override
    public void continues() {
        if (mediaPlayer!=null) {
            mediaPlayer.start();
            changeUILinstner.changePlay(Play.PLAY);
            notificationHelper.playingNotification(mp3Info,true);
        }
    }

    @Override
    public void stop() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    @Override
    public void next(int type) {

    }

    @Override
    public void previous() {

    }

    @Override
    public void selectPlay() {
        if (mp3Info==null){
            return;
        }

        if (mediaPlayer.isPlaying()){
            pause();
        }else {
            continues();
        }
    }

    @Override
    public void setPlatStatus(PlayStatus playStatus) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationHelper=new NotificationHelper();
        playHelper=PlayHelper.getPlayHelper();
        mediaPlayer=new MediaPlayer();
        //获取音频焦点
        AudioManager audioManager= (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playHelper.next(PlayHelper.AUTO);
            }
        });

        mp3Database=Mp3Database.getMp3Database();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange){
            case AudioManager.AUDIOFOCUS_GAIN://已获得音频焦点
                if (mediaPlayer!=null&&mp3Info!=null){
                    continues();
                }

                break;
            case AudioManager.AUDIOFOCUS_LOSS://失去音频焦点
                playHelper.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://临时失去焦点
                playHelper.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://临时失去焦点，但允许低音量播放
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.setVolume(0.1f,0.1f);//把声音调低
                }
                break;
        }
    }

    public class PlayBind extends Binder {
        public PlayService getPlayService(){
            if (playService==null) {
                return playService=PlayService.this;
            }
            return playService;
        }
    }

    public static PlayService getPlayService() {
        if (playService==null){
            return null;
        }
        return playService;
    }

    public PlayService() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            stop();
            mediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

      return new PlayBind();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 回调方法，改变UI
     * @param changeLinstner
     */
    public void setOnChangeLinstner(ChangeUILinstner changeLinstner){
        this.changeUILinstner=changeLinstner;
    }

    /**
     * 保存到播放历史当中
     * @param mp3Info
     */
    private void saveHistory(Mp3Info mp3Info){
//        mp3Database.createTable(HISTORYMUSIC);
        mp3Database.addDataToTable(HISTORYMUSIC,mp3Info);
    }




}
