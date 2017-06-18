package com.example.legend.lmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.legend.lmusic.R;
import com.example.legend.lmusic.model.SettingMenu;
import java.util.ArrayList;

/**
 *设置界面Adapter
 * Created by legend on 2017/6/13.
 */

public class SettingAdapter extends BaseAdapter<SettingAdapter.ViewHolder> {

    ArrayList<SettingMenu> settingMenus=new ArrayList<>();
    IShowDialog showDialog;

    public void setShowDialog(IShowDialog showDialog) {
        this.showDialog = showDialog;
    }

    public void setSettingMenus(ArrayList<SettingMenu> settingMenus) {
        this.settingMenus = settingMenus;
        notifyDataSetChanged();
    }

    public SettingAdapter() {
        super();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.setting_list,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewHolder.getAdapterPosition()){
                    case 0:
                        showDialog.showDialog(SettingType.CHANGE_THEME);
                        break;
                    case 1:
                        showDialog.showDialog(SettingType.CHANGE_MODE);

                        break;

                    case 2:

                        break;

                    default:

                        break;
                }
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SettingMenu settingMenu=settingMenus.get(position);
        holder.title.setText(settingMenu.getTitle());
        holder.subTitle.setText(settingMenu.getSubTitle());

    }

    @Override
    public int getItemCount() {
        if (settingMenus==null){

            return 0;
        }

        return settingMenus.size();
    }

    static class ViewHolder extends BaseAdapter.ViewHolder{

        View view;
        TextView title,subTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            title= (TextView) view.findViewById(R.id.setting_title);
            subTitle= (TextView) view.findViewById(R.id.setting_sub_title);
        }
    }

}
