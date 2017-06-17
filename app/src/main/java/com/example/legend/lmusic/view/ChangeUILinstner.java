package com.example.legend.lmusic.view;

import com.example.legend.lmusic.controller.PlayStatus;
import com.example.legend.lmusic.model.Mp3Info;

/**
 *
 * Created by legend on 2017/6/10.
 */

public interface ChangeUILinstner {
    void changeUI(PlayStatus playStatus);
    void changeInfo(Mp3Info mp3Info);
    void changePlay(Play play);
//    void change
}
