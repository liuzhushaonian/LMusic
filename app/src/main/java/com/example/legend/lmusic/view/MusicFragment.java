package com.example.legend.lmusic.view;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.example.legend.lmusic.R;
import com.example.legend.lmusic.adapter.ChangeList;
import com.example.legend.lmusic.adapter.MusicListAdapter;
import com.example.legend.lmusic.adapter.OpenFragmentLinstener;
import com.example.legend.lmusic.adapter.Type;
import com.example.legend.lmusic.controller.ControlMode;
import com.example.legend.lmusic.model.ImageLoader;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Database;
import com.example.legend.lmusic.model.Mp3Info;
import com.example.legend.lmusic.utils.Mp3Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.Inflater;


/**
 * 显示用户所有音乐、专辑、及其艺术家
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends BaseFragment implements OpenFragmentLinstener{

    private static final int CAN_CHANGE=0x00001;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ChangeList changeList;
    private ChangeListOnListenter changeListOnListenter;

    public void setChangeListOnListenter(ChangeListOnListenter changeListOnListenter) {
        this.changeListOnListenter = changeListOnListenter;
    }

    private static final String[] permissionStrings=
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


    //单例模式
    private static MusicFragment musicFragment;
    private ArrayList<Mp3Info> mp3InfoArrayList;


    public static MusicFragment newInstance(){
        if (musicFragment==null){
            return new MusicFragment();
        }
        return musicFragment;
    }


    public MusicFragment() {
        changeList=new MusicListAdapter();
//        checkPermission();
        // Required empty public constructor
    }

    //获取数据
    private void getData(){
        new Thread(){
            @Override
            public void run() {
                mp3InfoArrayList= Mp3Utils.getMp3InfoArrayList(LApplication.getContext());
                Message message=new Message();
                message.what=CAN_CHANGE;
                handler.sendMessage(message);
            }
        }.start();
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(LApplication.getContext(),
                permissionStrings[0])!= PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);

        }else {
            getData();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_data, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recycler);
        linearLayoutManager=new LinearLayoutManager(LApplication.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        changeList.setType(Type.NORMAL);
        recyclerView.setAdapter((MusicListAdapter) changeList);
        ((MusicListAdapter) changeList).setOpenFragmentLinstener(this);
        checkPermission();

//        ((MusicListAdapter) changeList).viewHolder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int position=((MusicListAdapter) changeList).viewHolder.getAdapterPosition();
//                Mp3Info mp3Info=mp3InfoArrayList.get(position);
//                playHelper.setMp3Infos(mp3InfoArrayList);
//                playHelper.play(mp3Info);
//                System.out.println("可以播放~~~~~~~~~");
//            }
//        });




        return view;

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CAN_CHANGE:
                    changeList.change(mp3InfoArrayList);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            mp3Database.dropTable(Mp3Database.DEFAULTTABLE);//删除默认表
                            mp3Database.createTable(Mp3Database.DEFAULTTABLE);
                            for (Mp3Info mp3Info:mp3InfoArrayList)
                            mp3Database.addDataToTable(Mp3Database.DEFAULTTABLE,mp3Info);
                        }

                    }.start();

                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getData();
                }else {
                    Toast.makeText(LApplication.getContext(),"无法获取权限，应用即将退出~",Toast.LENGTH_LONG).show();
                    new Thread(){
                        public void run(){
                            try {
                                Thread.sleep(2000);
                                Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }


    @Override
    public void openFragmentFromFragment(Mp3Info mp3Info) {
        openFragment(mp3Info);
    }

    @Override
    public void openFragmentFromFragment(String string) {

    }

    /**
     * 回调方法，响应长按事件弹出窗口
     * @param mp3Info
     */
    @Override
    public void popupMenu(final Mp3Info mp3Info) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        final String[] items={"添加到列表","保存专辑封面图","删除"};
        Bitmap bitmap=imageLoader.getBitmapFromFile(mp3Info);
        if (bitmap==null){
            BitmapDrawable bitmapDrawable= (BitmapDrawable) ContextCompat.getDrawable(LApplication.getContext(),R.drawable.notification);
            bitmap=bitmapDrawable.getBitmap();
        }
        builder.setIcon(new BitmapDrawable(getResources(),bitmap)).setTitle(mp3Info.getSongName()).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        popupList(mp3Info);
                        break;
                    case 1:
                        Bitmap bitmap1=imageLoader.getBitmapFromFile(mp3Info);
                        saveBitmap(bitmap1);
                        break;
                    case 2:
//                        File file=new File(mp3Info.getUrl());
//                        if (file.exists()){
//                            file.delete();
                            mp3InfoArrayList.remove(mp3Info);
                            changeList.change(mp3InfoArrayList);
//                        }
                        break;


                }
            }
        });
        builder.create().show();
    }

    @Override
    public void popupMenu(String table) {

    }

    private void saveMusic(Mp3Info mp3Info){

    }

    /**
     * 弹出已存在列表
     * @param mp3Info
     */
    private void popupList(final Mp3Info mp3Info){
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        ArrayList<String> strings=mp3Database.getList();
        String[] items=strings.toArray(new String[0]);
        final String[] strings1=new String[items.length+1];
        for (int i=0;i<items.length;i++){
            strings1[i]=items[i];
        }
        strings1[items.length]="添加新列表";
        final int l=strings1.length;
        builder.setTitle(mp3Info.getSongName()).setItems(strings1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String list=strings1[which];
                switch (list){
                    case "添加新列表":
                        popupEdit(mp3Info);
                        break;
                    default:
                        mp3Database.addDataToTable(list,mp3Info);
                        change();
                        Toast.makeText(LApplication.getContext(),"添加音乐成功~",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * 弹出编辑新列表名称弹窗
     * @param mp3Info
     */
    private void popupEdit(final Mp3Info mp3Info){
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        final EditText editText= new EditText(getContext());

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String list=editText.getText().toString();
                if (list.equals("")){
                    Toast.makeText(LApplication.getContext(),"列表名称不能为空",Toast.LENGTH_SHORT).show();
                }else {


                    int i=mp3Database.addList(list);
                    if (i==1) {
                        mp3Database.createTable(list);
                        mp3Database.addDataToTable(list, mp3Info);
                        change();
                        Toast.makeText(LApplication.getContext(), "添加音乐成功~", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LApplication.getContext(), "已存在相同列表，无法新建", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setView(editText).setTitle("新建列表名称");
        builder.show();

    }

    private void change(){
        changeListOnListenter.change();
    }



}
