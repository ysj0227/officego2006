package com.officego.utils;

import android.nfc.Tag;
import android.util.Log;

import com.google.android.material.appbar.AppBarLayout;
import com.officego.commonlib.utils.log.LogCat;

/**
 * Created by YangShiJie
 * Data 2020/5/12.
 * Descriptions:
 **/
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        //LogCat.e("TAG", "dec=" + Math.abs(i)+"; appBarLayout="+appBarLayout.getTotalScrollRange());
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED,i);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED,i);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE,i);
            }
            mCurrentState = State.IDLE;
        }
        onStateAbs(Math.abs(i));
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state,int offset);
    public abstract void onStateAbs(int abs);
}
