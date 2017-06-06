package com.example.legend.lmusic.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.adapter.ViewPagerSingleAdapter;

public class HomeActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private ViewPagerSingleAdapter viewPagerSingleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        prepared();
    }

    private void findId(){
        tabLayout= (TabLayout) findViewById(R.id.tab_layout);
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        viewPagerSingleAdapter=new ViewPagerSingleAdapter(getSupportFragmentManager());

    }

    private void prepared(){
        findId();
        dataPrepared();
        clickTab();
        pagerChanged();
    }


    private void clickTab(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void pagerChanged(){
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }
        });
    }

    private void dataPrepared(){
        tabLayout.addTab(tabLayout.newTab().setText("音乐"));
        tabLayout.addTab(tabLayout.newTab().setText("播放列表"));
        viewPager.setAdapter(viewPagerSingleAdapter);
    }

}
