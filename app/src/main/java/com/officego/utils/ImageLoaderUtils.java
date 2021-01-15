package com.officego.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.officego.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by YangShiJie
 * Data 2020/5/12.
 * Descriptions:
 **/
public class ImageLoaderUtils extends ImageLoader {
    private final Context mContext;

    public ImageLoaderUtils(Context context) {
        this.mContext = context;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(mContext).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.ic_def_banner)).load((String) path).into(imageView);
    }
}