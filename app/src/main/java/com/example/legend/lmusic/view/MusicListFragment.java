package com.example.legend.lmusic.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.adapter.MusicListAdapter;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Info;

import java.util.ArrayList;

/**
 * 显示列表音乐
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends BaseFragment {

    private String playListName;
    Toolbar toolbar;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MusicListAdapter musicListAdapter;
    private ArrayList<Mp3Info> mp3InfoArrayList;


    public void setPlayListName(String playListName) {
        this.playListName = playListName;
        if (playListName.equals("历史播放")) {
            mp3InfoArrayList = mp3Database.getNameList("history");
        } else {
            mp3InfoArrayList = mp3Database.getNameList(playListName);//从数据库获取音乐
        }

    }

    public MusicListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.music_list_recycler);
        linearLayoutManager = new LinearLayoutManager(LApplication.getContext());
        musicListAdapter = new MusicListAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(musicListAdapter);
        toolbar = (Toolbar) view.findViewById(R.id.music_toolbar);
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
        setToolbar();
        musicListAdapter.change(mp3InfoArrayList);
        return view;
    }

    private void setToolbar() {
        toolbar.inflateMenu(R.menu.home_menu);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle(playListName);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
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
                removeFragment(MusicListFragment.this);
            }
        });


    }
}
