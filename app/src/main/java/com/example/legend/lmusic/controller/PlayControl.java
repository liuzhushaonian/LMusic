package com.example.legend.lmusic.controller;

import com.example.legend.lmusic.model.Mp3Info;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * Created by legend on 2017/6/8.
 */

public interface PlayControl {
    void play(Mp3Info mp3Info) throws IOException;
    void pause();
    void continues();
    void stop();
    void next(int type);
    void previous();
    void selectPlay();
    void setPlatStatus(PlayStatus playStatus);
}
