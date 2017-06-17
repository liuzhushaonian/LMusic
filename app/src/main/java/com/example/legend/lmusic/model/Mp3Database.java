package com.example.legend.lmusic.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * 创建数据库，以实现对Mp3Info对象的永久保存，且将各个播放列表也一并保存
 * Created by liuzhushaonian on 2017/5/4.
 */

public class Mp3Database extends SQLiteOpenHelper{

    private static final String CMUSICDATABASE="mp3Database";//数据库名称

    private static final String SONG="songName";//歌名

    private static final String ARTIST="artist";//歌手

    private static final String ALBUM="album";//专辑

    private static final String TIME="time";//时长

    private static final String DATA="data";//绝对路径

    private static final String ALBUMSID="albumsId";//专辑ID

    private static final String ARTISTID="artistId";//歌手ID

//    private static final String ISALBUMBOOK="isAlbumBook";//有无专辑封面

    private static Mp3Database mp3Database;//当前类实例

    private static int VERSION=1;//数据库版本

    private SQLiteDatabase sqLiteDatabase;//数据库实例

    public static final String DEFAULTTABLE="AllMp3List";

    private static final String PLAYLIST="playList";
    private static final String HISTORY="historySearch";
    private static final String HISTORYMUSIC="history";


    /**
     * 单例模式
     * @return 返回当前类实例
     */
    public static Mp3Database getMp3Database(){
        if (mp3Database!=null){
            return mp3Database;
        }
        return mp3Database=new Mp3Database(LApplication.getContext(),CMUSICDATABASE,null,VERSION);
    }


    /**
     * 默认表，第一次创建数据库后即创建，存放用户所有的音乐信息
     */
    public static final String DEFAULT="CREATE TABLE IF NOT EXISTS "+DEFAULTTABLE+"(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "songName TEXT," +
            "artist TEXT," +
            "album TEXT," +
            "time INTEGER," +
            "data TEXT," +
            "albumsId INTEGER," +
            "artistId INTEGER," +
            "isAlbumBook INTEGER)";



    private Mp3Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        sqLiteDatabase=getReadableDatabase();//获取数据库实例
        createList();//创建默认表以及默认列表
    }

    /**
     * 创建默认表，保存用户所有的音乐信息
     * @param db 传入数据库名字
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DEFAULT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * 创建表，保存其他音乐列表
     * tableName为传入的表名字
     * @param tableName 传入表的名字以建立新表
     */
    public void createTable(String tableName) {

        try {
            String table = "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "songName TEXT," +
                    "artist TEXT," +
                    "album TEXT," +
                    "time INTEGER," +
                    "data TEXT," +
                    "albumsId INTEGER," +
                    "artistId INTEGER)";
            sqLiteDatabase.execSQL(table);
        }catch (Exception e){
            //e.fillInStackTrace();
            System.out.println("the table is exist!");
        }
    }

    /**
     * 创建普通表，存放指定数据，比如输入历史等等
     * @param tableName
     *
     */
    public void createHistorySearchTable(String tableName){


        try {
            String table = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,historyInput TEXT NOT NULL UNIQUE)";
            sqLiteDatabase.execSQL(table);
        }catch (Exception e){
            //e.fillInStackTrace();
            System.out.println("the table is exist!");
        }
    }



    public void addHistoryString(String historyString){
        try {
            String sql="insert into "+HISTORY+" (historyInput) values ('"+historyString+"')";
            sqLiteDatabase.execSQL(sql);
        }catch (Exception e){
            System.out.println("the table is exist!");
        }

    }





    /**
     * 创建已有列表的名字的表，提供fragmentList获取数据使用，仅仅是用户
     */
    public void createList(){
        try {
            String table = "CREATE TABLE IF NOT EXISTS " + PLAYLIST + "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "listName TEXT NOT NULL UNIQUE)";
            sqLiteDatabase.execSQL(table);

            String sql="insert into "+PLAYLIST+" (listName) values ('历史播放')";
            sqLiteDatabase.execSQL(sql);
            createTable(HISTORYMUSIC);//创建播放历史表单

        }catch (Exception e){
            //e.fillInStackTrace();
            System.out.println("the table is exist!");
        }
    }

    /**
     * 查询历史
     *
     * @return
     */
    public ArrayList<String> queryHistory(String key){
        ArrayList<String> strings=new ArrayList<>();
        String sql="select "+key+" from "+HISTORY;
        Cursor cursor=sqLiteDatabase.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
            do {
                String string=cursor.getString(cursor.getColumnIndex(key));
                System.out.println("得到了名称---------------"+string);
                strings.add(string);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return strings;
    }





    /**
     * 给表增加数据
     * @param list
     */
    public int addList(String list){
        int i=0;

        try {
            String sql="insert into "+PLAYLIST+" (listName) values ('"+list+"')";
            sqLiteDatabase.execSQL(sql);
            i=1;
            return i;
        }catch (Exception e){
            e.fillInStackTrace();
            return 0;
        }

    }

    public ArrayList<String> getList(){
        ArrayList<String> strings=new ArrayList<>();
        String sql="select listName from "+PLAYLIST;
        Cursor cursor=sqLiteDatabase.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
            do {
                String string=cursor.getString(cursor.getColumnIndex("listName"));
                System.out.println("得到了名称---------------"+string);
                strings.add(string);


            }while (cursor.moveToNext());
        }
        cursor.close();

        return strings;
    }

    public void deleteList(String list){
        try {
            String sql="delete from "+PLAYLIST+" where listName = '"+list+"'";
            sqLiteDatabase.execSQL(sql);
            System.out.println("chanchuchenggong!!!!");
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }


    /**
     * 查询表中所有数据并返回对应列表
     * @param tableName 表示传入的表名字，如果没有则返回
     * @return 返回一个ArrayList
     */
    public ArrayList<Mp3Info> getNameList(String tableName){
        ArrayList<Mp3Info> mp3Infos=new ArrayList<>();
        if (tableName.equals("历史播放")){
            tableName=HISTORYMUSIC;
        }


        Cursor cursor_name=sqLiteDatabase.query(tableName,null,null,null,null,null,null);
        if (cursor_name.moveToFirst()) {
            do {
                Mp3Info mp3Info = new Mp3Info();
                mp3Info.setAlbumName(cursor_name.getString(cursor_name.getColumnIndex(ALBUM)));
                mp3Info.setAlbumId(cursor_name.getInt(cursor_name.getColumnIndex(ALBUMSID)));
                mp3Info.setArtist(cursor_name.getString(cursor_name.getColumnIndex(ARTIST)));
                mp3Info.setUrl(cursor_name.getString(cursor_name.getColumnIndex(DATA)));
                mp3Info.setSongName(cursor_name.getString(cursor_name.getColumnIndex(SONG)));
                mp3Info.setTime(cursor_name.getLong(cursor_name.getColumnIndex(TIME)));
                mp3Infos.add(mp3Info);
            }while (cursor_name.moveToNext());
        }
        cursor_name.close();

        return mp3Infos;
    }


    /**
     * 添加数据
     * @param table 添加到的表
     * @param mp3Info 传入MP3对象
     */
    public void addDataToTable(String table, Mp3Info mp3Info){

//        createTable(table);
        ContentValues contentValues=new ContentValues();
        contentValues.put(SONG,mp3Info.getSongName());
        contentValues.put(ARTIST,mp3Info.getArtist());
        contentValues.put(ALBUM,mp3Info.getAlbumName());
        contentValues.put(TIME,mp3Info.getTime());
        contentValues.put(DATA,mp3Info.getUrl());
        contentValues.put(ALBUMSID,mp3Info.getAlbumId());
        contentValues.put(ARTISTID,mp3Info.getArtistId());
//        if (mp3Info.isHas_albums_book()){
//            contentValues.put(ISALBUMBOOK,1);
//        }else {
//            contentValues.put(ISALBUMBOOK,0);
//        }

        sqLiteDatabase.insert(table,null,contentValues);
        contentValues.clear();

    }


    /**
     * 删除表
     * @param table 传入表名字
     */
    public void dropTable(String table){
        String sql="DROP TABLE IF EXISTS "+table;
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * 暂时未想到可以更新什么有效数据，所以这个方法置空保留
     * @param table
     * @param mp3Info
     */
    public void upDate(String table,Mp3Info mp3Info){

    }

    /**
     * 删除数据
     * @param table 需要删除数据所在的表
     * @param mp3Info 需要删除的MP3对象
     */
    public void deleteDate(String table,Mp3Info mp3Info){

        //安卓提供的方法
//        sqLiteDatabase.delete(table,SONG+"=?",new String[]{mp3Info.getSongs_name()});

        //原生SQL语句
        try {
            String sql="DELETE FROM "+table+" WHERE "+SONG+"="+"'"+mp3Info.getSongName()+"'";
            sqLiteDatabase.execSQL(sql);

            System.out.println("删除成功！");
        }catch (Exception e){
            System.out.println("删除失败");
        }



    }

    //查询数据库，返回MP3对象数组
    public ArrayList<ArrayList> queryDate(String massage){
        ArrayList<ArrayList> arrayList=new ArrayList<>();
        arrayList.add(getSongs(massage));
        arrayList.add(getArtist(massage));
        arrayList.add(getAlbums(massage));

        return arrayList;
    }


    private ArrayList<Mp3Info> getAlbums(String albums){
        ArrayList<Mp3Info> mp3InfoArrayList=new ArrayList<>();
        String sql_name="SELECT * FROM "+DEFAULTTABLE+" WHERE "+ALBUM+" LIKE "+"'%"+albums+"%'";
        Cursor cursor_name=sqLiteDatabase.rawQuery(sql_name,null);

        if (cursor_name.moveToFirst()) {
            do {
                Mp3Info mp3Info = new Mp3Info();
                mp3Info.setAlbumName(cursor_name.getString(cursor_name.getColumnIndex(ALBUM)));
                mp3Info.setAlbumId(cursor_name.getInt(cursor_name.getColumnIndex(ALBUMSID)));
                mp3Info.setArtist(cursor_name.getString(cursor_name.getColumnIndex(ARTIST)));
                mp3Info.setUrl(cursor_name.getString(cursor_name.getColumnIndex(DATA)));
                mp3Info.setSongName(cursor_name.getString(cursor_name.getColumnIndex(SONG)));
                mp3Info.setTime(cursor_name.getLong(cursor_name.getColumnIndex(TIME)));
                mp3InfoArrayList.add(mp3Info);
            }while (cursor_name.moveToNext());
        }
        cursor_name.close();
        return mp3InfoArrayList;
    }

    private ArrayList<Mp3Info> getArtist(String artist){
        ArrayList<Mp3Info> mp3InfoArrayList=new ArrayList<>();
        String sql_name="SELECT * FROM "+DEFAULTTABLE+" WHERE "+ARTIST+" LIKE "+"'%"+artist+"%'";
        Cursor cursor_name=sqLiteDatabase.rawQuery(sql_name,null);

        if (cursor_name.moveToFirst()) {
            do {
                Mp3Info mp3Info = new Mp3Info();
                mp3Info.setAlbumName(cursor_name.getString(cursor_name.getColumnIndex(ALBUM)));
                mp3Info.setAlbumId(cursor_name.getInt(cursor_name.getColumnIndex(ALBUMSID)));
                mp3Info.setArtist(cursor_name.getString(cursor_name.getColumnIndex(ARTIST)));
                mp3Info.setUrl(cursor_name.getString(cursor_name.getColumnIndex(DATA)));
                mp3Info.setSongName(cursor_name.getString(cursor_name.getColumnIndex(SONG)));
                mp3Info.setTime(cursor_name.getLong(cursor_name.getColumnIndex(TIME)));
                mp3InfoArrayList.add(mp3Info);
            }while (cursor_name.moveToNext());
        }
        cursor_name.close();
        return mp3InfoArrayList;
    }

    public ArrayList<Mp3Info> getSongs(String song){
        ArrayList<Mp3Info> mp3InfoArrayList=new ArrayList<>();
        String sql_name="SELECT * FROM "+DEFAULTTABLE+" WHERE "+SONG+" LIKE "+"'%"+song+"%'";
        Cursor cursor_name=sqLiteDatabase.rawQuery(sql_name,null);

        if (cursor_name.moveToFirst()) {
            do {
                Mp3Info mp3Info = new Mp3Info();
                mp3Info.setAlbumName(cursor_name.getString(cursor_name.getColumnIndex(ALBUM)));
                mp3Info.setAlbumId(cursor_name.getInt(cursor_name.getColumnIndex(ALBUMSID)));
                mp3Info.setArtist(cursor_name.getString(cursor_name.getColumnIndex(ARTIST)));
                mp3Info.setUrl(cursor_name.getString(cursor_name.getColumnIndex(DATA)));
                mp3Info.setSongName(cursor_name.getString(cursor_name.getColumnIndex(SONG)));
                mp3Info.setTime(cursor_name.getLong(cursor_name.getColumnIndex(TIME)));
                mp3InfoArrayList.add(mp3Info);
            }while (cursor_name.moveToNext());
        }
        cursor_name.close();
        return mp3InfoArrayList;
    }




    /**
     * 测试方法，实际当中不使用！！
     * @param name
     * @return
     */
//    public String getData(String name){
//        String data=null;
//        String sql_name="SELECT * FROM allMusic WHERE "+SONG+"='"+name+"'";
//        Cursor cursor_name=sqLiteDatabase.rawQuery(sql_name,new String[]{});
//        while (cursor_name.moveToNext()){
////        for (int i=0;i<cursor_name.getCount();i++) {
//            data = cursor_name.getString(cursor_name.getColumnIndex(DATA));
//            System.out.println(data);
//        }
////        }
////        System.out.println("Mp3Database.getData222");
//
//        return data;
//
//    }





}
