package com.example.legend.lmusic.model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.controller.ControlMode;
import com.example.legend.lmusic.controller.SettingBean;

/**
 *
 * Created by legend on 2017/6/6.
 */

public class LApplication extends Application{

    private static Context context;
    public static ControlMode controlMode=ControlMode.CONTROL_VERTICAL;//默认控制模式———>水平的，任性~
    private static Theme musicTheme=Theme.DEFAULT;//主题
    protected static SharedPreferences.Editor editor;
    protected static SharedPreferences sharedPreferences;
    protected final static String CONFIG="config";
    private final static String MODE="mode";
    private final static String THEME="theme";
    private static Bitmap bitmap;

    //设置控制模式
    public static ControlMode getControlMode() {
        return controlMode;
    }

    //保存控制模式
    public static void setControlMode(ControlMode controlMode) {
        LApplication.controlMode = controlMode;
//        switch (controlMode){
//            case CONTROL_HORIZONTAL:
//                editor.putString(MODE,"CONTROL_HORIZONTAL");
//                break;
//            case CONTROL_VERTICAL:
//                editor.putString(MODE,"CONTROL_VERTICAL");
//                break;
//
//        }

        editor.putString(MODE,controlMode.name());

        editor.apply();//提交

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        /**
         * 初始化两个SharedPreferences对象，提供读取与写入
         */
        editor=getSharedPreferences(CONFIG,MODE_PRIVATE).edit();
        sharedPreferences=getSharedPreferences(CONFIG,MODE_PRIVATE);
        /**
         * 初始化控制模式
         */
        String mode=sharedPreferences.getString(MODE,"CONTROL_HORIZONTAL");

        switch (mode){
            case "CONTROL_HORIZONTAL":
                controlMode=ControlMode.CONTROL_HORIZONTAL;
                break;
            case "CONTROL_VERTICAL":
                controlMode=ControlMode.CONTROL_VERTICAL;
                break;
        }

        /**
         * 初始化主题
         */
        String theme=sharedPreferences.getString(THEME,"DEFAULT");

        switch (theme){
            case "DEFAULT":
                musicTheme=Theme.DEFAULT;
                break;
            case "YELLOW":
                musicTheme=Theme.YELLOW;
                break;
            case "PINK":
                musicTheme=Theme.PINK;
                break;
            case "DIY":
                musicTheme=Theme.DIY;
                break;
        }
    }

    public static Context getContext() {
        return context;
    }

    public static int getHeight(){
        int height= (int) getContext().getResources().getDimension(R.dimen.bottomHeight);
        return height;
    }

    public static int getWidth(){
        int width= (int) getContext().getResources().getDimension(R.dimen.albumsWidth);
        return width;

    }

    public static int getMargin(){
        int margin= (int) getContext().getResources().getDimension(R.dimen.text_margin);
        return margin;
    }

    //设置主题，保存主题
    public static Theme getMusicTheme() {
        return musicTheme;
    }

    public static void setMusicTheme(Theme musicTheme) {
        LApplication.musicTheme = musicTheme;
        editor.putString(THEME,musicTheme.name());
        editor.apply();
    }

    /**
     * 提供屏幕的长与宽
     * @return
     */
    public static int getScreenWidth(){
        int width=getDisplay().widthPixels;

        return width;
    }

    public static int getScreenHeight(){
        int height=getDisplay().heightPixels;

        return height;
    }

    private static DisplayMetrics getDisplay(){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

}
