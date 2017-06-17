package com.example.legend.lmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.legend.lmusic.R;
import java.util.ArrayList;

/**
 *
 * Created by legend on 2017/6/7.
 */

public class PlayListAdapter extends BaseAdapter<PlayListAdapter.ViewHolder>{


    private ArrayList<String> stringArrayList;
    OpenFragmentLinstener openFragmentLinstener;

    public void setOpenFragmentLinstener(OpenFragmentLinstener openFragmentLinstener) {
        this.openFragmentLinstener = openFragmentLinstener;
    }

    public PlayListAdapter() {
        super();
    }

    public void setStringArrayList(ArrayList<String> stringArrayList) {
        if (stringArrayList!=null) {
            this.stringArrayList = stringArrayList;
            System.out.println("没空");
        }else {
            this.stringArrayList=mp3Database.getList();
            System.out.println("空的！！！");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.play_list,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string=viewHolder.textView.getText().toString();
                openFragmentLinstener.openFragmentFromFragment(string);
            }
        });

        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position=viewHolder.getAdapterPosition();
                String table=stringArrayList.get(position);
                openFragmentLinstener.popupMenu(table);
                return true;
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String listNmae=stringArrayList.get(position);
        holder.textView.setText(listNmae);
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }


    static class ViewHolder extends BaseAdapter.ViewHolder {
        View view;
        ImageView imageView;
        TextView textView;


        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            imageView= (ImageView) view.findViewById(R.id.list_image);
            textView= (TextView) view.findViewById(R.id.list_name);

        }
    }



}
