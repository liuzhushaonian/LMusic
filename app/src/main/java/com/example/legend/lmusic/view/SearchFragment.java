package com.example.legend.lmusic.view;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.adapter.ChangeList;
import com.example.legend.lmusic.adapter.OnputLinstener;
import com.example.legend.lmusic.adapter.SearchAdapter;
import com.example.legend.lmusic.adapter.Type;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Info;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements OnputLinstener{

    ArrayList<String> historyList;//保存输入历史
    ArrayList<Mp3Info> historyMp3Infos;//保存搜索到的音乐
    SearchView searchView;
    Toolbar toolbar;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SearchAdapter searchAdapter;
    String historyString;
    EditText editText;



    private static final String HISTORY="historySearch";
    private static final String key="historyInput";

    public SearchFragment() {
        // Required empty public constructor
        mp3Database.createHistorySearchTable(HISTORY);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);

        searchView= (SearchView) view.findViewById(R.id.search);
        toolbar= (Toolbar) view.findViewById(R.id.search_toolbar);
        switch (LApplication.getMusicTheme()){
            case DIY:
                resetToolbar(toolbar);
                view.setBackground(getDrawable());
                break;
            default:
                toolbar.setBackground(getDrawable());
                resetColor(view);
                break;
        }

        int id = searchView.getResources().getIdentifier("search_src_text", "id", getActivity().getPackageName());

        final EditText textView = (EditText ) searchView.findViewById(id);
        editText=textView;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment(SearchFragment.this);
                closeSoftKeybord(textView);

            }
        });
        searchView.setIconified(false);
        historyList=mp3Database.queryHistory(key);
        recyclerView= (RecyclerView) view.findViewById(R.id.search_list);
        linearLayoutManager=new LinearLayoutManager(LApplication.getContext());
        searchAdapter=new SearchAdapter();
//        searchAdapter.setType(Type.NORMAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchAdapter);
        searchAdapter.setOnputLinstener(this);
//        setHistory();

//        int completeTextId = searchView.getResources().getIdentifier("android:id/search_src_text", null, null);


        /**
         * 查询
         */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMusic(query);
                String historyString=query;
                mp3Database.addHistoryString(historyString);
                closeSoftKeybord(textView);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchMusic(newText);
//                System.out.println("输入中-----------"+textView.toString());

                return true;
            }
        });
        return view;
    }

    private void searchMusic(String string){
        if (string.equals("")){
            return;
        }
        historyMp3Infos=mp3Database.getSongs(string);

        searchAdapter.setType(Type.ALBUM);
        searchAdapter.change(historyMp3Infos);
    }


    //关闭输入法
    private void closeSoftKeybord(View view){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    @Override
    public void putString(String string) {
        editText.setText(string);
        searchMusic(string);
    }


}
