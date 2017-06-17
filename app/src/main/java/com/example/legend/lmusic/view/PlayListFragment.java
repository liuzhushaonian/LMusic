package com.example.legend.lmusic.view;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.adapter.OpenFragmentLinstener;
import com.example.legend.lmusic.adapter.PlayListAdapter;
import com.example.legend.lmusic.model.ImageSelector;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Info;

import java.util.ArrayList;

/**
 * 显示播放列表、历史播放以及最近播放
 * A simple {@link Fragment} subclass.
 */
public class PlayListFragment extends BaseFragment implements OpenFragmentLinstener,ChangeListOnListenter{

    private LinearLayoutManager linearLayoutManager;
    private PlayListAdapter playListAdapter;
    RecyclerView recyclerView;
    private ArrayList<String> strings;
    Button button;
    BezierView bezierView;




    //单例模式
    private static PlayListFragment playListFragment;


    public PlayListFragment() {

        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        strings=mp3Database.getList();
    }


    public static PlayListFragment newInstance(){
        if (playListFragment==null){
            return new PlayListFragment();
        }
        return playListFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_play_list, container, false);

        playListAdapter=new PlayListAdapter();
        recyclerView= (RecyclerView) view.findViewById(R.id.user_play_list);
        playListAdapter.setStringArrayList(strings);
        linearLayoutManager=new LinearLayoutManager(LApplication.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(playListAdapter);
        playListAdapter.setOpenFragmentLinstener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void openFragmentFromFragment(Mp3Info mp3Info) {
        //
    }

    @Override
    public void openFragmentFromFragment(String string) {
        MusicListFragment musicListFragment=new MusicListFragment();

        openFragment(musicListFragment);
        musicListFragment.setPlayListName(string);
    }

    @Override
    public void popupMenu(Mp3Info mp3Info) {

    }

    @Override
    public void popupMenu(final String table) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        final String[] items={"播放列表","新建列表","删除列表"};

        builder.setTitle(table).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        ArrayList<Mp3Info> mp3Infos=mp3Database.getNameList(table);
                        playHelper.setPlayMusic(mp3Infos);
                        break;
                    case 1:
                        popupEdit();
                        break;
                    case 2:
                        mp3Database.dropTable(table);
                        mp3Database.deleteList(table);
                        change();
                        break;
                }
            }
        });
        builder.create().show();

    }

    //改变列表显示
    public void changeList(){
        strings=mp3Database.getList();
        playListAdapter.setStringArrayList(strings);
        playListAdapter.notifyDataSetChanged();

    }

    @Override
    public void change() {
        changeList();
    }

    private void popupEdit(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        final EditText editText= new EditText(getContext());

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String list=editText.getText().toString();
                if (list.equals("")){
                    Toast.makeText(LApplication.getContext(),"列表名称不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    mp3Database.addList(list);
                    mp3Database.createTable(list);
                    change();
                    Toast.makeText(LApplication.getContext(),"添加列表成功~",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(editText).setTitle("新建列表名称");
        builder.show();

    }

}
