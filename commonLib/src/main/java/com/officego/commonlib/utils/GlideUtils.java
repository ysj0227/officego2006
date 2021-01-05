package com.officego.commonlib.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
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
     *
     * @param context
     * @param view
     * @param url
     */
    public static void urlToDrawable(Context context, View view, String url) {
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

    //获取数据
    public static void loadCheckBoxDrawable(Context context, String path, CheckBox view) {
        RoundedCorners roundedCorners = new RoundedCorners(6);
        Glide.with(context).load(path).apply(new RequestOptions()
                .bitmapTransform(roundedCorners)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(38, 38)).into(new ViewTarget<TextView, Drawable>(view) {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                view.setCompoundDrawablesWithIntrinsicBounds(null, resource, null, null);
            }
        });
    }
}
