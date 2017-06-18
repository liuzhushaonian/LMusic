package com.example.legend.lmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Info;
import com.example.legend.lmusic.view.CircleImage;

import java.util.ArrayList;

/**
 *
 * Created by legend on 2017/6/13.
 */

public class SearchAdapter extends BaseAdapter<BaseAdapter.ViewHolder> implements ChangeList{

    ArrayList<String> historyString=new ArrayList<>();
    ArrayList<Mp3Info> mp3Infos=new ArrayList<>();
    Type type=Type.NORMAL;
    OnputLinstener onputLinstener;

    public void setOnputLinstener(OnputLinstener onputLinstener) {
        this.onputLinstener = onputLinstener;
    }

    public SearchAdapter() {
        super();
    }

    public void setHistoryString(ArrayList<String> historyString) {
        this.historyString = historyString;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (type==Type.NORMAL) {
            //搜索之前
            View view = layoutInflater.inflate(R.layout.play_list, parent, false);
            final HistoryViewHolder viewHolder = new HistoryViewHolder(view);
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = viewHolder.textView.getText().toString();
                    onputLinstener.putString(s);
                }
            });


            return viewHolder;
        }else {
            //搜索之后
                View view1=layoutInflater.inflate(R.layout.music_list,parent,false);
                final MusicViewHolder viewHolder1=new MusicViewHolder(view1);

            viewHolder1.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=viewHolder1.getAdapterPosition();
                    Mp3Info mp3Info=mp3Infos.get(position);
                    ArrayList<Mp3Info> mp3InfoArrayList=new ArrayList<>();
                    mp3InfoArrayList.add(mp3Info);
//                    playHelper.setMp3Infos(mp3InfoArrayList);
//                    playHelper.play(mp3Info);
                    playMusic(mp3InfoArrayList,mp3Info,position);
                }
            });


                return viewHolder1;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        switch (type){
            case NORMAL:
                String history=historyString.get(position);
                if (holder instanceof HistoryViewHolder){
                    ((HistoryViewHolder) holder).textView.setText(history);
                }

                break;
            default:
                Mp3Info mp3Info=mp3Infos.get(position);
                if (holder instanceof MusicViewHolder){
                    ((MusicViewHolder) holder).songName.setText(mp3Info.getSongName());
                    ((MusicViewHolder) holder).artist.setText(mp3Info.getArtist());
                    imageLoader.bindBitmap(mp3Info,((MusicViewHolder) holder).circleImage, LApplication.getWidth(),LApplication.getHeight());
                }
                break;
        }


    }

    @Override
    public int getItemCount() {
        switch (type){
            case NORMAL:
                return historyString.size();
            default:
                return mp3Infos.size();
        }

    }

    @Override
    public void setType(Type type) {
        this.type=type;
    }

    /**
     * 接口方法改变搜索后显示的内容，必须连同type一起改变才行
     * @param mp3Infos
     */
    @Override
    public void change(ArrayList<Mp3Info> mp3Infos) {
        this.mp3Infos=mp3Infos;
        notifyDataSetChanged();
    }


    static class HistoryViewHolder extends BaseAdapter.ViewHolder{

        TextView textView;
        View view;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            textView= (TextView) view.findViewById(R.id.list_name);
        }
    }


    static class MusicViewHolder extends BaseAdapter.ViewHolder{

        CircleImage circleImage;
        TextView songName,artist;
        View view;
        public MusicViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            circleImage= (CircleImage) view.findViewById(R.id.albums_image);
            songName= (TextView) view.findViewById(R.id.song_name);
            artist= (TextView) view.findViewById(R.id.artist);
        }
    }
}
