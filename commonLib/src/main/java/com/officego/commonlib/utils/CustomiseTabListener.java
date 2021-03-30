package com.officego.commonlib.utils;

import com.google.android.material.tabs.TabLayout;

/**
 * Created by shijie
 * Date 2021/3/16
 **/
public abstract class CustomiseTabListener implements TabLayout.OnTabSelectedListener {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        onTabSelected(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    protected abstract void onTabSelected(int position);
}
