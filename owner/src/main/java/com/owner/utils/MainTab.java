package com.owner.utils;

import com.owner.R;
import com.owner.fragment.HomeFragment_;
import com.owner.message.MessageFragment_;
import com.owner.mine.MineFragment_;
import com.owner.schedule.ScheduleFragment_;

public enum MainTab {
    HOME(
            R.string.str_tab_home,
            R.drawable.ic_tab_home,
            HomeFragment_.class
    ),

    MESSAGE(
            R.string.str_tab_message,
            R.drawable.ic_tab_message,
            MessageFragment_.class

    ),

    COLLECT(
            R.string.str_tab_schedule,
            R.drawable.ic_tab_collect,
            ScheduleFragment_.class
    ),

    MINE(
            R.string.str_tab_mine,
            R.drawable.ic_tab_mine,
            MineFragment_.class
    );

    private int resName;
    private int resIcon;
    private Class<?> clz;

    MainTab(int resName, int resIcon, Class<?> clz) {
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

}
