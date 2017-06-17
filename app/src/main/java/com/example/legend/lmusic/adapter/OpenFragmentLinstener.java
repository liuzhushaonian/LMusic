package com.example.legend.lmusic.adapter;

import com.example.legend.lmusic.model.Mp3Info;

/**
 *
 * Created by legend on 2017/6/13.
 */

public interface OpenFragmentLinstener {
    void openFragmentFromFragment(Mp3Info mp3Info);
    void openFragmentFromFragment(String string);
    void popupMenu(Mp3Info mp3Info);
    void popupMenu(String table);
}
