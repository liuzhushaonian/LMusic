package com.example.legend.lmusic.view;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.adapter.IShowDialog;
import com.example.legend.lmusic.adapter.SettingAdapter;
import com.example.legend.lmusic.adapter.SettingType;
import com.example.legend.lmusic.controller.ControlMode;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.SettingMenu;
import com.example.legend.lmusic.model.Theme;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class SettingActivity extends BaseActivity implements IShowDialog{
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Toolbar toolbar;
    SettingAdapter settingAdapter;
    private static final int CAN_LOAD=0x000001;
//    Intent intent;
//    Messenger messenger;
    private Bitmap bigBitmap;
    private Bitmap smallBitmap;
    RelativeLayout relativeLayout;


    ArrayList<SettingMenu> settingMenus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        recyclerView= (RecyclerView) findViewById(R.id.setting);
        toolbar= (Toolbar) findViewById(R.id.setting_toolbar);
        relativeLayout= (RelativeLayout) findViewById(R.id.all_background);
        setToolbar();
        prepareData();//准备数据

//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null){
            return;
        }
        switch (requestCode){
            case 200:
                startCropImage(data.getData(),LApplication.getScreenWidth(),LApplication.getScreenHeight());
                break;
            case 300:


                break;
            case BIG:
                bigBitmap= decodeUriBitmap(data.getData());
                //开线程保存文件
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        saveBitmap(bigBitmap);
                    }
                }.start();
                //saveBitmap(bigBitmap);
                Message message=new Message();
                message.what=0;
                mHandler.sendMessage(message);
                break;
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    //执行替换背景操作

                    resetToolbar();
                    relativeLayout.setBackground(new BitmapDrawable(getResources(),bigBitmap));
                    Intent intent=new Intent();
                    intent.putExtra("theme", 3);
                    LApplication.setMusicTheme(Theme.DIY);
                    setResult(400,intent);
                    break;
            }
        }
    };

    private Bitmap decodeUriBitmap(Uri uri){
        Bitmap bitmap=null;

        try {
            bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void prepareData(){
        linearLayoutManager=new LinearLayoutManager(this);
        settingAdapter=new SettingAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(settingAdapter);
        settingAdapter.setShowDialog(this);
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle("设置");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back);
        switch (LApplication.getMusicTheme()) {
            case DIY:
                resetToolbar();
                relativeLayout.setBackground(getDrawable());
                break;
            default:
                toolbar.setBackground(getDrawable());
                resetColor();
                break;
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    /**
     * 重写返回键事件
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread() {
            @Override
            public void run() {
                super.run();
                settingMenus = new ArrayList<>();
                SettingMenu controlMode = new SettingMenu();
                controlMode.setTitle(getString(R.string.controlMode));
                controlMode.setSubTitle(getString(R.string.sub_controlMode));
                SettingMenu theme = new SettingMenu();
                theme.setTitle(getString(R.string.theme));
                theme.setSubTitle(getString(R.string.sub_theme));
                SettingMenu imageChange = new SettingMenu();
                imageChange.setTitle(getString(R.string.controlImageDIY));
                imageChange.setSubTitle(getString(R.string.sub_controlImageDIY));
                settingMenus.add(theme);
                settingMenus.add(controlMode);
                Message message=new Message();
                message.what=CAN_LOAD;
                handler.sendMessage(message);
            }
        }.start();
    }

    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CAN_LOAD:
                    settingAdapter.setSettingMenus(settingMenus);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };


    @Override
    public void showDialog(SettingType settingType) {
        openDialog(settingType);
    }


    private void openDialog(SettingType settingType){

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);

        switch (settingType){
            case CHANGE_IMAGE:

                break;
            case CHANGE_MODE:
                final String[] items={"水平模式","垂直模式"};
                builder.setTitle(getString(R.string.controlMode)).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                LApplication.setControlMode(ControlMode.CONTROL_HORIZONTAL);
                                Toast.makeText(SettingActivity.this,"已设置控制为水平模式",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                LApplication.setControlMode(ControlMode.CONTROL_VERTICAL);
                                Toast.makeText(SettingActivity.this,"已设置控制为垂直模式",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                builder.create().show();
                break;
            case CHANGE_THEME:

                LayoutInflater layoutInflater=getLayoutInflater();
                final ViewGroup viewGroup= (ViewGroup) layoutInflater.inflate(R.layout.dialog_theme,null,false);


                final AlertDialog alertDialog= builder.setTitle("设置主题").setView(viewGroup).show();


                for (int i=0;i<=3;i++){
                    final View view=viewGroup.getChildAt(i);
                    view.setOnClickListener(new View.OnClickListener() {
                        Intent intent=new Intent();


                        @Override
                        public void onClick(View v) {
                            switch (view.getId()){
                                case R.id.default_theme:
                                    intent.putExtra("theme", 0);
                                    setTheme(Theme.DEFAULT);

                                    changeTheme(getDrawable());
                                    resetColor();
                                    setResult(400,intent);
                                    break;
                                case R.id.yellow_theme:
                                    intent.putExtra("theme", 1);
                                    setTheme(Theme.YELLOW);

                                    changeTheme(getDrawable());
                                    resetColor();
                                    setResult(400,intent);
                                    break;
                                case R.id.pink_theme:
                                    intent.putExtra("theme", 2);
                                    setTheme(Theme.PINK);
                                    changeTheme(getDrawable());
                                    resetColor();
                                    setResult(400,intent);
                                    break;
                                case R.id.diy_theme:
                                    openAlbum();


//                                    intent.putExtra("theme", 3);
//                                    LApplication.setMusicTheme(Theme.DIY);
//                                    setResult(400,intent);
                                    break;
                            }
                            alertDialog.dismiss();//点击后使AlertDialog消失

                        }
                    });
                }

                alertDialog.show();
                break;
            default:
                break;
        }
    }

    private void setTheme(Theme theme){
        LApplication.setMusicTheme(theme);
    }


    //
    private void changeActivityBackground(){

    }

    public void changeTheme(Drawable color) {
        toolbar.setBackground(color);
    }


    public void resetToolbar() {
        toolbar.setBackgroundColor(Color.alpha(0));
    }

    private void resetColor(){
        relativeLayout.setBackgroundColor(ContextCompat.getColor(SettingActivity.this,R.color.colorWhite));
    }


}
