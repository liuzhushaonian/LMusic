package com.example.legend.lmusic.adapter;

import com.example.legend.lmusic.model.Mp3Info;

import java.util.ArrayList;

/**
 *
 * Created by legend on 2017/6/6.
 */

public interface ChangeList {
    void setType(Type type);
    void change(ArrayList<Mp3Info> mp3Infos);
}
