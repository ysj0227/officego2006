package com.officego.commonlib.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.officego.commonlib.R;

/**
 * Created by YangShiJie
 * Data 2020/7/1.
 * Descriptions:
 **/
public class GlideUtils {
    //列表默认图片
    public static RequestOptions options() {
        return new RequestOptions()
                .error(R.mipmap.ic_loading_def_bg)
                .placeholder(R.mipmap.ic_loading_def_bg);
    }
    //头像默认图片
    public static RequestOptions avaOoptions() {
        return new RequestOptions()
                .error(R.mipmap.default_avatar)
                .placeholder(R.mipmap.default_avatar);
    }

    /**
     * url 转化 Drawable
     * @param context
     * @param view
     * @param url
     */
    public static void urlToDrawable(Context context, View view,String url) {
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                view.setBackground(resource);
            }
        };
        Glide.with(context)
                .load(url)
                .error(R.mipmap.ic_default_load)
                .into(simpleTarget);
    }

}
