package com.example.legend.lmusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 *
 * Created by legend on 2017/6/6.
 */

public class ViewPagerSingleAdapter extends FragmentStatePagerAdapter{

//    private ArrayLis



    public ViewPagerSingleAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
