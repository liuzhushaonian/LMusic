package com.example.legend.lmusic.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.controller.PlayHelper;
import com.example.legend.lmusic.controller.SettingBean;
import com.example.legend.lmusic.model.ImageLoader;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Database;
import com.example.legend.lmusic.service.PlayService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class BaseActivity extends AppCompatActivity{


    protected Messenger messenger;
    protected ImageLoader imageLoader;
    protected PlayHelper playHelper;
    protected PlayService playService;
    boolean isBind=false;
    protected Mp3Database mp3Database;
    protected Uri imageUri;
    protected final static int BIG=500;
    protected final String IMAGE_PATH= String.valueOf(LApplication.getContext().getFilesDir());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        mp3Database=Mp3Database.getMp3Database();

        imageLoader=ImageLoader.getImageLoader();
//        mPresenter.attachView((V) this);
        playHelper=PlayHelper.getPlayHelper();
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void openFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.full_screen,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected void openActivity(Intent intent){
//        startActivityForResult(intent,);
    }

    protected void openAlbum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,200);
    }

    /**
     * 剪切图片，获取小图
     * @param uri
     */

    protected void startCropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", LApplication.getWidth());
        intent.putExtra("outputY", LApplication.getWidth());

        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                photoOutputUri = Uri.parse("file:////sdcard/image_output.jpg"));

        intent.putExtra("outImage", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 300);
    }



    protected void startCropImage(Uri uri,int w,int h) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", w);
        intent.putExtra("aspectY", h);
        //输出图片的宽高均为150
        intent.putExtra("outputX", w);
        intent.putExtra("outputY", h);

        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                photoOutputUri = Uri.parse("file:////sdcard/image_output.jpg"));

        intent.putExtra("outImage", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection",true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent, BIG);
    }


    protected Drawable getDrawable(){
        Drawable drawable=null;
        switch (LApplication.getMusicTheme()){
            case DEFAULT:
                drawable= ContextCompat.getDrawable(BaseActivity.this,R.drawable.bg_default);
                break;
            case YELLOW:
                drawable= ContextCompat.getDrawable(BaseActivity.this,R.drawable.bg_orange);
                break;
            case PINK:
                drawable= ContextCompat.getDrawable(BaseActivity.this,R.drawable.bg_pink);
                break;
            case DIY:
                Bitmap bitmap=imageLoader.getBitmap();
                if (bitmap!=null) {

                    drawable = new BitmapDrawable(getResources(), imageLoader.getBitmap());
                }else {
                    drawable=ContextCompat.getDrawable(BaseActivity.this,R.drawable.huaji);
                }
                break;

        }

        return drawable;
    }

    protected void saveBitmap(Bitmap bitmap){
        FileOutputStream fileOutputStream=null;

        try {
            File file=new File(IMAGE_PATH);
            if (!file.exists()){
                file.mkdirs();
            }
            File file1=new File(file,"bg");


            fileOutputStream=new FileOutputStream(file1);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,fileOutputStream);
            fileOutputStream.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }







}
