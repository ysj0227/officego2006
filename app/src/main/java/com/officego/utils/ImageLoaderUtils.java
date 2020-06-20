package com.officego.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by YangShiJie
 * Data 2020/5/12.
 * Descriptions:
 **/
public class ImageLoaderUtils extends ImageLoader {
    private Context mContext;
    public ImageLoaderUtils(Context context){
        this.mContext=context;
    }
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(mContext).load((String) path).into(imageView);
    }
}