package com.example.legend.lmusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.legend.lmusic.view.MusicFragment;
import com.example.legend.lmusic.view.PlayListFragment;

import java.util.ArrayList;

/**
 *负责显示两个fragment，分别是musicFragment以及PlayListFragment
 * Created by legend on 2017/6/6.
 */

public class ViewPagerSingleAdapter extends FragmentStatePagerAdapter{

    ArrayList<Fragment> fragments;
    MusicFragment musicFragment;
    PlayListFragment playListFragment;

//    private ArrayLis



    public ViewPagerSingleAdapter(FragmentManager fm) {
        super(fm);
        playListFragment=PlayListFragment.newInstance();
        musicFragment=MusicFragment.newInstance();
        musicFragment.setChangeListOnListenter(playListFragment);
        fragments=new ArrayList<>();
        fragments.add(musicFragment);
        fragments.add(playListFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
