package com.example.legend.lmusic.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;

import com.example.legend.lmusic.controller.PlayStatus;

/**
 *
 * Created by legend on 2017/6/6.
 */

public interface OnChangeThemeListener {
    void changeTheme(int color);

    void changeTheme(Bitmap bitmap);

}
