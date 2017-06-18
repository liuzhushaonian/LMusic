package com.example.legend.lmusic.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.adapter.ViewPagerSingleAdapter;
import com.example.legend.lmusic.controller.PlayControl;
import com.example.legend.lmusic.controller.PlayHelper;
import com.example.legend.lmusic.model.ImageLoader;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.service.PlayService;
import com.example.legend.lmusic.utils.AnimationUtil;
import com.example.legend.lmusic.utils.CircleBitmapCut;
import com.example.legend.lmusic.model.Mp3Info;
import com.example.legend.lmusic.controller.PlayStatus;

import java.io.File;
import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements View.OnClickListener,ChangeUILinstner,View.OnLongClickListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private ViewPagerSingleAdapter viewPagerSingleAdapter;
    ImageView imageView;
    Uri photoOutputUri;
    BezierView viewPlay,viewPrevious,viewNext,viewStatus,viewSetting;//定义可移动的view
    private ArrayList<View> views;
    private AnimationUtil animationUtil;
    private CircleImage controller;
    private int color;
    VDHLayout vdhLayout;
    Bundle bundle;

    private static final String TAG="HomeActivity";

    private void log(String s){
        Log.d(TAG, "log: "+s);
    }


    private ArrayList<PlayStatus> playStatuses;



    private void resumeView(){
        if (bundle!=null){
            int tab=bundle.getInt("tab_position");
            tabLayout.getTabAt(tab).select();
            viewPager.setCurrentItem(tab);
        }

        if (playHelper.getCurrentMp3Info()!=null) {
            changeUI(playHelper.getPlayStatus());
            changePlay(playHelper.getPlay());
            changeInfo(playHelper.getCurrentMp3Info());

            route();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, PlayService.class));
        bindPlayService();
        log("onCreate");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int tab_position=tabLayout.getSelectedTabPosition();
        outState.putInt("tab_position",tab_position);
        bundle=outState;
        log("saveData");
    }



    @Override
    protected void onResume() {
        super.onResume();

        playStatuses=new ArrayList<>();
        playStatuses.add(PlayStatus.PLAY_ORDERLY);
        playStatuses.add(PlayStatus.PLAY_CYCLE_LIST);
        playStatuses.add(PlayStatus.PLAY_CYCLE_ONE);
        playStatuses.add(PlayStatus.PLAY_RANDOM);

        resumeView();
        log("onResume");

    }

    private void route(){
        if (playHelper.getCurrentMp3Info()!=null) {
            switch (playHelper.getPlay()){
                case PLAY:
                    startRotate(controller);
                    break;
                case PAUSE:
                    startRotate(controller);
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepared();
        viewPagerSingleAdapter=new ViewPagerSingleAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerSingleAdapter);
        log("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        log("onStop");
    }



    private void findId(){
        vdhLayout= (VDHLayout) findViewById(R.id.full_screen);
        tabLayout= (TabLayout) findViewById(R.id.tab_layout);
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        viewPlay= (BezierView) findViewById(R.id.view_play);
        viewPrevious= (BezierView) findViewById(R.id.view_previous);
        viewNext= (BezierView) findViewById(R.id.view_next);
        viewStatus= (BezierView) findViewById(R.id.view_status);
        controller= (CircleImage) findViewById(R.id.controller);
        viewSetting= (BezierView) findViewById(R.id.setting);
//        Bitmap bitmap=((BitmapDrawable)getResources().getDrawable(R.drawable.ran)).getBitmap();
        viewPlay.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.play),false);
        viewPrevious.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.pre),false);
        viewNext.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.next),false);
        viewStatus.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.off),false);
        viewSetting.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.setting),false);
        views=new ArrayList<>();
        views.add(viewStatus);
        views.add(viewNext);
        views.add(viewPlay);
        views.add(viewPrevious);
        animationUtil=new AnimationUtil();
//        setOnclick(views);
        controller.setOnClickListener(this);
        viewPlay.setOnClickListener(this);
        viewStatus.setOnClickListener(this);
        viewNext.setOnClickListener(this);
        viewPrevious.setOnClickListener(this);
        viewSetting.setOnClickListener(this);
        controller.setOnLongClickListener(this);

    }


    private void reDraw(){
        if (playHelper.getCurrentMp3Info()!=null){
            toolbar.setTitle(playHelper.getCurrentMp3Info().getSongName());
        }else {
            toolbar.setTitle("音乐");
        }
        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPink));
        switch (LApplication.getMusicTheme()){
            case DIY:
                resetToolbar();
                vdhLayout.setBackground(getDrawable());
                break;
            default:
                toolbar.setBackground(getDrawable());
                tabLayout.setBackground(getDrawable());
                resetColor();

                break;
        }


        setSupportActionBar(toolbar);

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
    }

    private void resetColor(){
        vdhLayout.setBackgroundColor(ContextCompat.getColor(HomeActivity.this,R.color.colorWhite));
    }

    public void resetToolbar() {
        toolbar.setBackgroundColor(Color.alpha(0));
        tabLayout.setBackgroundColor(Color.alpha(0));
    }

    private void prepared(){
        findId();
        dataPrepared();
        clickTab();
        pagerChanged();
        reDraw();
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
        if (tabLayout.getTabCount()==0) {
            tabLayout.addTab(tabLayout.newTab().setText("音乐"));
            tabLayout.addTab(tabLayout.newTab().setText("播放列表"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindPlayService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause");
    }





    @Override
    public void changeUI(PlayStatus playStatus) {
        switch (playStatus){

            case PLAY_CYCLE_LIST:
                viewStatus.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.circle),true);
                playHelper.setPlatStatus(PlayStatus.PLAY_CYCLE_LIST);

                break;
            case PLAY_CYCLE_ONE:
                viewStatus.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.one),true);
                playHelper.setPlatStatus(PlayStatus.PLAY_CYCLE_ONE);

                break;
            case PLAY_ORDERLY:
                viewStatus.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.off),true);
                playHelper.setPlatStatus(PlayStatus.PLAY_ORDERLY);
                break;
            case PLAY_RANDOM:
                viewStatus.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.ran),true);
                playHelper.setPlatStatus(PlayStatus.PLAY_RANDOM);

                break;
            default:
                viewStatus.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.off),true);

                break;

        }
    }

    @Override
    public void changeInfo(Mp3Info mp3Info) {
        toolbar.setTitle(mp3Info.getSongName());
        toolbar.setSubtitle(mp3Info.getArtist());
        imageLoader.bindBitmap(mp3Info,controller,controller.getMeasuredWidth(),controller.getMeasuredHeight());
    }

    @Override
    public void changePlay(Play play) {
        switch (play) {
            case PLAY:
                viewPlay.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.pause), true);
                startRotate(controller);

                break;
            case PAUSE:
                viewPlay.setBitmap(CircleBitmapCut.getBitmapIcon(R.drawable.play), true);
                stopRotate(controller);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 400:
                if (data==null){
                    return;
                }
                int color=data.getIntExtra("theme",0);
                switch (color) {
                    case 0:

                        changeTheme(ContextCompat.getDrawable(HomeActivity.this, R.drawable.bg_default));
                        break;
                    case 1:

                        changeTheme(ContextCompat.getDrawable(HomeActivity.this, R.drawable.bg_orange));
                        break;
                    case 2:
                        changeTheme(ContextCompat.getDrawable(HomeActivity.this, R.drawable.bg_pink));
                        break;
                    case 3:
                        Bitmap bitmap=imageLoader.getBitmap();
                        if (bitmap!=null){
                            vdhLayout.setBackground(new BitmapDrawable(getResources(),bitmap));
                        }else {
                            changeTheme(ContextCompat.getDrawable(HomeActivity.this, R.drawable.bg_default));
                        }


//                        changeTheme(ContextCompat.getDrawable(HomeActivity.this, R.drawable));
                        break;

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    private  boolean isClose=false;
    private int position=0;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.controller:
                if (isSettingOpened){
                    closeSetting(viewSetting);
                }else {

                    if (isClose) {

                        for (int i = 0; i < views.size(); i++) {
                            animationUtil.closeMenu(views.get(i), i);
                        }
                        isClose = false;

                    } else {
                        for (int i = 0; i < views.size(); i++) {
                            animationUtil.openMenuHorizontal(views.get(i), i);
                            ((BezierView) views.get(i)).startFactorAnimation(500, 0, 1);

                        }
                        isClose = true;


                    }
                }

                break;
            case R.id.view_play:
                playService.selectPlay();

//                viewPlay.startFactorAnimation(500,0,1);
//                startAnimation(v);
                break;
            case R.id.view_status:
//                viewStatus.startFactorAnimation(500,0,1);
                position++;
                if (position>3){
                    position=0;
                }
                changeUI(playStatuses.get(position));
//                System.out.println(views.get(0).getId()==v.getId());
                break;

            case R.id.view_next:
//                viewNext.startFactorAnimation(500,0,1);
                playHelper.next(PlayHelper.UI);

                break;
            case R.id.view_previous:
//                viewPrevious.startFactorAnimation(500,0,1);
                playHelper.previous();
                break;
            case R.id.setting:
                Intent intent=new Intent(HomeActivity.this,SettingActivity.class);
//                Bundle bundle=new Bundle();
//                IBinder iBinder=messenger.getBinder();
//                bundle.putBinder("binder",iBinder);
//                intent.putExtras(bundle);
//                openActivity(intent);

                startActivityForResult(intent,400);
//                viewSetting.startFactorAnimation(500,0,1);
                closeSetting(viewSetting);
                break;
        }
        if (v instanceof BezierView) {
            startAnimation((BezierView) v);
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);

    }






    private void startAnimation(BezierView view){
        view.startFactorAnimation(500,0,1);
    }





    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService.PlayBind playBind= (PlayService.PlayBind) service;
            playService=playBind.getPlayService();

            playService.setOnChangeLinstner(HomeActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger=null;
            isBind=false;
        }
    };


    //绑定Service
    public void bindPlayService(){
        if (!isBind){
            Intent intent=new Intent(this,PlayService.class);
            bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);

            isBind=true;

        }
    }

    //解除绑定
    public void unbindPlayService(){
        if (isBind){
            unbindService(serviceConnection);
            isBind=false;

        }
    }



    private void startRotate(View view){
        animationUtil.startRotate(view,true);
    }

    private void stopRotate(View view){
        animationUtil.startRotate(view,false);
    }


    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()){
            case R.id.controller:
                openSetting();
                return true;
            default:

                break;
        }

        return true;
    }

    private boolean isSettingOpened=false;


    private void openSetting(){
        animationUtil.popupSetting(viewSetting);
        isSettingOpened=true;
    }

    private void closeSetting(View view){
        animationUtil.closeSetting(view);
        isSettingOpened=false;
    }

    /**
     *修改颜色
     * @param color
     */

    public void changeTheme(Drawable color) {
        tabLayout.setBackground(color);
        toolbar.setBackground(color);
    }


    public void changeTheme(Bitmap bitmap) {
        tabLayout.setBackgroundColor(Color.alpha(0));
        toolbar.setBackgroundColor(Color.alpha(0));
    }


//    private Messenger messenger=new Messenger(new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            System.out.println("收到的消息是---------"+msg.what);
//        }
//    });



}
