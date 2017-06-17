package com.example.legend.lmusic.controller;

/**
 *
 *保存设置信息
 * Created by legend on 2017/6/15.
 */

public class SettingBean {

    private String theme;//主题
    private String mode;//控制模式
    private boolean isDiy;//是否使用自定义主题

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }


}
