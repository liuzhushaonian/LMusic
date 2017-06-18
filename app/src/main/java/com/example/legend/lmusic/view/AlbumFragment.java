package com.example.legend.lmusic.view;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.adapter.ChangeList;
import com.example.legend.lmusic.adapter.MusicListAdapter;
import com.example.legend.lmusic.adapter.Type;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Info;
import com.example.legend.lmusic.utils.Mp3Utils;

import java.util.ArrayList;

/**
 * 专辑fragment
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends BaseFragment {

    private Mp3Info mp3Info;
    private TextView album_music_name,album_music_info,album_music_time;
    private ImageView albumBook;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ChangeList musicListAdapter;
    private ArrayList<Mp3Info> mp3Infos;
    private Toolbar toolbar;
    private Activity activity;
    private AppCompatActivity appCompatActivity;


    public AlbumFragment() {
        // Required empty public constructor
        activity=getActivity();

    }

    public void setMp3Info(Mp3Info mp3Info) {
        this.mp3Info = mp3Info;
        mp3Infos= Mp3Utils.getAlbumMusic(mp3Info.getAlbumName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_album, container, false);
        album_music_name= (TextView) view.findViewById(R.id.album_music_name);
        album_music_info= (TextView) view.findViewById(R.id.album_music_info);
        album_music_time= (TextView) view.findViewById(R.id.album_music_time);
        albumBook= (ImageView) view.findViewById(R.id.album_music_image);
        recyclerView= (RecyclerView) view.findViewById(R.id.album_music_recyclerView);

        toolbar= (Toolbar) view.findViewById(R.id.album_toolbar);
        switch (LApplication.getMusicTheme()){
            case DIY:
                resetToolbar(toolbar);
                view.setBackground(getDrawable());
                break;
            default:
                toolbar.setBackground(getDrawable());
                resetColor(view);
                break;
        }
//        AppCompatActivity appCompatActivity= (AppCompatActivity) activity;
//        appCompatActivity.setSupportActionBar(toolbar);
//        ActionBar actionBar=appCompatActivity.getSupportActionBar();
//        if (actionBar!=null){
//            actionBar.setDisplayHomeAsUpEnabled(false);
//        }


        setToolbar();



        musicListAdapter=new MusicListAdapter();
        linearLayoutManager=new LinearLayoutManager(LApplication.getContext());
        album_music_name.setText(mp3Info.getAlbumName());
        album_music_info.setText(mp3Info.getArtist());
//        album_music_time.setText();
//        imageLoader.bindBitmap(mp3Info,albumBook,albumBook.getWidth(),albumBook.getHeight());

        Bitmap bitmap=imageLoader.getBitmapFromFile(mp3Info);
        int w = (int) LApplication.getContext().getResources().getDimension(R.dimen.albums);
        if (bitmap!=null) {

            bitmap = Bitmap.createScaledBitmap(bitmap, w, w, false);
        }else {
            Drawable drawable= ContextCompat.getDrawable(LApplication.getContext(),R.drawable.notification);
            BitmapDrawable bitmapDrawable= (BitmapDrawable) drawable;
            bitmap=bitmapDrawable.getBitmap();
            bitmap=Bitmap.createScaledBitmap(bitmap,w,w,false);
        }
        albumBook.setImageBitmap(bitmap);
        musicListAdapter.setType(Type.ALBUM);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter((RecyclerView.Adapter) musicListAdapter);
        musicListAdapter.change(mp3Infos);
        return view;
    }

    private void setToolbar(){
        toolbar.inflateMenu(R.menu.home_menu);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle(mp3Info.getAlbumName());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item_seartch:
                        openFragment(new SearchFragment());

                        break;
                }
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment(AlbumFragment.this);
            }
        });
    }






}
