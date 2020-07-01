package com.officego.commonlib.utils;

import com.bumptech.glide.request.RequestOptions;
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
}
