package com.officego.ui.home.animation;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by shijie
 * Date 2020/12/25
 **/
public class CustomRotateAnim extends Animation {
    /** 控件宽 */
    private int mWidth;
    /** 控件高 */
    private int mHeight;

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        this.mWidth = width;
        this.mHeight = height;
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        // 左右摇摆
        t.getMatrix().setRotate((float)(Math.sin(interpolatedTime*Math.PI*2)*40), mWidth/2, mHeight/2);
        super.applyTransformation(interpolatedTime, t);
    }
}

