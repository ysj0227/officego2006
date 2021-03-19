package com.officego.utils;

/**
 * Created by shijie
 * Date 2021/3/19
 **/

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.core.widget.NestedScrollView;

public class MapContainer extends RelativeLayout {
    private NestedScrollView scrollView;

    public MapContainer(Context context) {
        super(context);
    }

    public MapContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollView(NestedScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        scrollView.requestDisallowInterceptTouchEvent(ev.getAction() != MotionEvent.ACTION_UP);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
