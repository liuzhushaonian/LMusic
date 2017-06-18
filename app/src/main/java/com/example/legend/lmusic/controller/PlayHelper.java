package com.example.legend.lmusic.controller;

import com.example.legend.lmusic.controller.PlayControl;
import com.example.legend.lmusic.model.Mp3Info;
import com.example.legend.lmusic.service.PlayService;
import com.example.legend.lmusic.view.Play;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * 播放模式以及各种判断帮助，持有播放器接口实例
 * Created by legend on 2017/6/8.
 */

public class PlayHelper implements PlayControl{

    private ArrayList<Mp3Info> mp3Infos;
    private static PlayControl player;
    private Mp3Info currentMp3Info;//记录当前正在播放的音乐
    private int currentPosition;//记录当前播放的位置
    private PlayStatus playStatus=PlayStatus.PLAY_ORDERLY;
    private static PlayHelper playHelper;

    public static final int UI=0x00001;
    public static final int AUTO=0x00002;
    private Play play=Play.PAUSE;

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public static PlayHelper getPlayHelper() {
        if (playHelper==null){
            return playHelper=new PlayHelper();
        }
        return playHelper;
    }

    public PlayStatus getPlayStatus() {
        return playStatus;
    }


    public ArrayList<Mp3Info> getMp3Infos() {
        return mp3Infos;
    }

    /**
     * 外部传入需要播放的列表
     * @param mp3Infos
     */
    public void setMp3Infos(ArrayList<Mp3Info> mp3Infos) {
        this.mp3Infos = mp3Infos;
    }


    public PlayHelper() {

    }

    //接口方法使用判断是否进行上一首或者下一首
    @Override
    public void next(int type) {


        if (mp3Infos==null){
            return;
        }

        //判断当前状态
        switch (playStatus){
            case PLAY_RANDOM://随机模式

                currentPosition=getCurrentPosition();
                if (currentPosition<0||currentPosition>mp3Infos.size()){
                    currentPosition=getCurrentPosition();
                }
                break;
            case PLAY_CYCLE_ONE://单曲循环
                if (type==AUTO){//正常播放完成后
                    play(currentMp3Info);
                }else {//手动点击下一首
                    if (currentPosition+1>mp3Infos.size()-1){
                        pause();
                    }else {
                        currentPosition++;
                        currentMp3Info=mp3Infos.get(currentPosition);
                        play(currentMp3Info);
                    }
                }

                break;
            default://其余模式
                if (currentPosition+1>mp3Infos.size()-1){//遇到最后一首歌曲时
                    switch (playStatus){
                        case PLAY_CYCLE_LIST://列表循环
                            currentPosition=0;
                            currentMp3Info=mp3Infos.get(currentPosition);
                            play(currentMp3Info);
                            break;
                        case PLAY_ORDERLY://列表顺序不循环
                            pause();
                            break;
                        default:
                            break;
                    }
                }else {//非最后一首
                    currentPosition++;
                    currentMp3Info=mp3Infos.get(currentPosition);
                    play(currentMp3Info);

                }
                break;
        }

    }


    @Override
    public void previous() {
        if (mp3Infos==null){
            return;

        }

        if (currentPosition-1<0){
            currentPosition=0;
        }else {
            currentPosition--;
        }

        currentMp3Info=mp3Infos.get(currentPosition);
        play(currentMp3Info);
    }

    @Override
    public void selectPlay() {
        if (currentMp3Info!=null){
            continues();
        }else if (mp3Infos==null){
            return;

        }else {
            player.selectPlay();
        }


    }

    @Override
    public void setPlatStatus(PlayStatus playStatus) {
        this.playStatus = playStatus;
    }

    //以下方法为代理模式
    @Override
    public void play(Mp3Info mp3Info) {
        getPlayer();


        try {
            player.play(mp3Info);
            currentMp3Info=mp3Info;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void continues() {
        player.continues();
    }

    @Override
    public void stop() {
        player.stop();
    }

    private boolean isNullPlay(){
        return player==null;
    }

    private void getPlayer(){
        if (isNullPlay()){
            player=PlayService.getPlayService();
        }
    }

    private int getCurrentPosition(){
        int max=mp3Infos.size();
        int min=0;
        Random random=new Random();
        int position=random.nextInt(max)%(max-min+1)+min;
        return position;
    }

    public void randomPlay(){
        currentPosition=getCurrentPosition();
        play(mp3Infos.get(currentPosition));
    }

    public Mp3Info getCurrentMp3Info() {
        return currentMp3Info;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setCurrentMp3Info(Mp3Info currentMp3Info) {
        this.currentMp3Info = currentMp3Info;
    }

    /**
     * 传入列表播放
     * @param mp3Infos
     */
    public void setPlayMusic(ArrayList<Mp3Info> mp3Infos){
        this.mp3Infos=mp3Infos;
        currentPosition=0;
        play(mp3Infos.get(currentPosition));
    }


}
