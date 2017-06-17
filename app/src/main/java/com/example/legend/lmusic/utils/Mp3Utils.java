package com.example.legend.lmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.legend.lmusic.model.Mp3Info;

import java.util.ArrayList;

/**
 * 检索数据库，并将搜索到的音乐封装为Mp3Info对象
 * Created by legend on 2017/6/6.
 */

public class Mp3Utils {
    private static ArrayList<Mp3Info> mp3InfoArrayList;

    public static ArrayList<Mp3Info> getMp3InfoArrayList(Context context){
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media.DURATION + ">=18000", null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        mp3InfoArrayList=new ArrayList<>();
        for (int i=0;i<cursor.getCount();i++){
            cursor.moveToNext();

            Mp3Info mp3Info=new Mp3Info();
            String uri=cursor.getString(cursor.getColumnIndex((MediaStore.Audio.Media.DATA)));
            long _id=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String albums=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long duration=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            int isMusic=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            int albumsId=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            long artistId=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));


            if (isMusic!=0){
                mp3Info.setSongName(title);
                mp3Info.setAlbumId(albumsId);
                mp3Info.setAlbumName(albums);
                mp3Info.setArtistId(artistId);
                mp3Info.setArtist(artist);
                mp3Info.setTime(duration);
                mp3Info.setUrl(uri);
                mp3InfoArrayList.add(mp3Info);
            }
        }
        cursor.close();

        return mp3InfoArrayList;


    }




    //转换时间
    public static String formatTime(long time){
        String min=time/(1000*60)+"";
        String sec=time%(1000*60)+"";
        if (min.length()<1){
            min="0"+time/(1000*60)+"";
        }else {
            min=time/(1000*60)+"";
        }
        if (sec.length()==4){
            sec="0"+(time%(1000*60))+"";
        }else if (sec.length()==3){
            sec="00"+(time%(1000*60))+"";
        }else if (sec.length()==2){
            sec="000"+(time%(1000*60))+"";
        }else if (sec.length()==1){
            sec="0000"+(time%(1000*60))+"";
        }
        return min+":"+sec.trim().substring(0,2);
    }

    public static ArrayList<Mp3Info> getAlbumMusic(String albums){
        ArrayList<Mp3Info> albumsList=new ArrayList<>();
        for (int i=0;i<mp3InfoArrayList.size();i++){
            if (albums.equals(mp3InfoArrayList.get(i).getAlbumName())){
                Mp3Info mp3Info=mp3InfoArrayList.get(i);
                albumsList.add(mp3Info);
            }
        }
        return albumsList;
    }

//    public static ArrayList<Mp3Info> getMusic(String song){
//        ArrayList<Mp3Info> mp3Infos=new ArrayList<>();
//
//        return mp3Infos;
//    }
}
