package com.example.legend.lmusic.view;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.controller.PlayHelper;
import com.example.legend.lmusic.model.ImageLoader;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Database;
import com.example.legend.lmusic.model.Mp3Info;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment implements OnChangeThemeListener {

    protected Mp3Database mp3Database;
    protected ImageLoader imageLoader;
    protected PlayHelper playHelper;
    protected static final String IMAGE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/LMusicImage";


    public BaseFragment() {

        mp3Database = Mp3Database.getMp3Database();
        imageLoader = ImageLoader.getImageLoader();
        playHelper = PlayHelper.getPlayHelper();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    /**
     * 添加一个fragment
     *
     * @param mp3Info 传入对象
     */
    protected void openFragment(Mp3Info mp3Info) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AlbumFragment albumFragment = new AlbumFragment();
        transaction.add(R.id.full_screen, albumFragment);
        albumFragment.setMp3Info(mp3Info);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * 移除一个fragment
     *
     * @param fragment 传入移除的fragment
     */
    protected void removeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentManager.popBackStack();//模拟栈操作，将栈顶null去掉
        fragmentTransaction.commit();
    }

    protected void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.full_screen, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void changeTheme(int color) {

    }

    @Override
    public void changeTheme(Bitmap bitmap) {

    }

    protected Drawable getDrawable() {
        Drawable drawable = null;
        switch (LApplication.getMusicTheme()) {
            case DEFAULT:
                drawable = ContextCompat.getDrawable(LApplication.getContext(), R.drawable.bg_default);
                break;
            case YELLOW:
                drawable = ContextCompat.getDrawable(LApplication.getContext(), R.drawable.bg_orange);
                break;
            case PINK:
                drawable = ContextCompat.getDrawable(LApplication.getContext(), R.drawable.bg_pink);
                break;
            case DIY:
                Bitmap bitmap = imageLoader.getBitmap();
                if (bitmap != null) {

                    drawable = new BitmapDrawable(getResources(), imageLoader.getBitmap());
                } else {
                    drawable = ContextCompat.getDrawable(LApplication.getContext(), R.drawable.huaji);
                }
                break;

        }

        return drawable;
    }

    protected void resetColor(View view) {
        view.setBackgroundColor(ContextCompat.getColor(LApplication.getContext(), R.color.colorWhite));
    }

    protected void resetToolbar(Toolbar toolbar) {
        toolbar.setBackgroundColor(Color.alpha(0));
    }

    protected void addNewList(String list) {
        mp3Database.addList(list);
    }

    protected void saveBitmap(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;

        try {
            File file = new File(IMAGE_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file1 = new File(file, SystemClock.currentThreadTimeMillis()+".jpg");


            fileOutputStream = new FileOutputStream(file1);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();

            Toast.makeText(LApplication.getContext(),"已保存在SD/LMusicImage/下",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
