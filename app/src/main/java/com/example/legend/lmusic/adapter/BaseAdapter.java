package com.example.legend.lmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.example.legend.lmusic.controller.PlayControl;
import com.example.legend.lmusic.controller.PlayHelper;
import com.example.legend.lmusic.model.ImageLoader;
import com.example.legend.lmusic.model.Mp3Database;
import com.example.legend.lmusic.model.Mp3Info;

import java.util.ArrayList;


class BaseAdapter<T extends BaseAdapter.ViewHolder> extends RecyclerView.Adapter<T>{

    protected ImageLoader imageLoader;
    protected PlayHelper playHelper;
    protected Mp3Database mp3Database;




    public BaseAdapter() {
        super();
        imageLoader=ImageLoader.getImageLoader();
        playHelper=PlayHelper.getPlayHelper();
        mp3Database=Mp3Database.getMp3Database();
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public void onBindViewHolder(T holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 统一播放接口
     * @param mp3Infos 传入播放列表
     * @param mp3Info 播放歌曲
     * @param position 播放歌曲所在列表位置
     */
    protected void playMusic(ArrayList<Mp3Info> mp3Infos,Mp3Info mp3Info, int position){
        playHelper.setMp3Infos(mp3Infos);
        playHelper.setCurrentPosition(position);
        playHelper.play(mp3Info);
    }




}
