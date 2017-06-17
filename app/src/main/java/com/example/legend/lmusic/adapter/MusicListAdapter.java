package com.example.legend.lmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.controller.PlayControl;
import com.example.legend.lmusic.controller.PlayHelper;
import com.example.legend.lmusic.model.ImageSelector;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Info;
import com.example.legend.lmusic.view.CircleImage;
import java.util.ArrayList;

/**
 *用于展示音乐列表的Adapter
 * Created by legend on 2017/6/6.
 */

public class MusicListAdapter extends BaseAdapter<MusicListAdapter.ViewHolder> implements ChangeList{

    private ArrayList<Mp3Info> mp3InfoArrayList=new ArrayList<>();

    public View view;

    private OpenFragmentLinstener openFragmentLinstener;

    public void setOpenFragmentLinstener(OpenFragmentLinstener openFragmentLinstener) {
        this.openFragmentLinstener = openFragmentLinstener;
    }


    private Type type=Type.NORMAL;

    @Override
    public void setType(Type type) {
        this.type = type;
    }


    public MusicListAdapter() {
        super();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view=layoutInflater.inflate(R.layout.music_list,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        switch (type){
            case NORMAL://如果是普通类型，则给图片以及歌手都加入点击事件
                viewHolder.albumsImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=viewHolder.getAdapterPosition();
                        Mp3Info mp3Info=mp3InfoArrayList.get(position);
                        openFragmentLinstener.openFragmentFromFragment(mp3Info);//使用回调方法打开fragment
                    }
                });
                viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position=viewHolder.getAdapterPosition();
                        Mp3Info mp3Info=mp3InfoArrayList.get(position);
                        openFragmentLinstener.popupMenu(mp3Info);
//                        Toast.makeText(LApplication.getContext(),"长按事件，这是第"+position+"个位置",Toast.LENGTH_SHORT).show();//


                        return true;
                    }
                });


                break;


            case ALBUM:


                break;
            default:
                break;
        }
        /**
         * 给整个view设置点击事件
         */
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImageSelector imageSelector=new ImageSelector();
                int position=viewHolder.getAdapterPosition();
                Mp3Info mp3Info=mp3InfoArrayList.get(position);
//                playHelper.setMp3Infos(mp3InfoArrayList);
//                playHelper.play(mp3Info);
                playMusic(mp3InfoArrayList,mp3Info,position);
            }
        });



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Mp3Info mp3Info=mp3InfoArrayList.get(position);
        holder.songName.setText(mp3Info.getSongName());
        holder.artist.setText(mp3Info.getArtist());
        imageLoader.bindBitmap(mp3Info,holder.albumsImage, LApplication.getWidth(),LApplication.getHeight());

    }

    @Override
    public int getItemCount() {
        return mp3InfoArrayList.size();
    }

    //接口方法，改变列表
    @Override
    public void change(ArrayList<Mp3Info> mp3Infos) {
        mp3InfoArrayList=mp3Infos;
        notifyDataSetChanged();
    }

    static class ViewHolder extends BaseAdapter.ViewHolder {
        private TextView songName;
        private TextView artist;
        private View view;

        private CircleImage albumsImage;


        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            songName= (TextView) itemView.findViewById(R.id.song_name);
            artist= (TextView) itemView.findViewById(R.id.artist);
            albumsImage= (CircleImage) itemView.findViewById(R.id.albums_image);
        }
    }


}
