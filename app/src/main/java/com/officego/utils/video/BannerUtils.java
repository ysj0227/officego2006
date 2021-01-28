package com.officego.utils.video;

import android.content.Context;

import com.officego.utils.ImageLoaderUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.List;

/**
 * Created by shijie
 * Date 2021/1/28
 **/
public class BannerUtils {
    /**
     * 详情轮播图
     */
    public static void set(Context context, Banner bannerImage, List<String> mBannerList) {
        //数字
        bannerImage.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器，图片加载器在下方
        bannerImage.setImageLoader(new ImageLoaderUtils(context));
        //设置图片网址或地址的集合
        bannerImage.setImages(mBannerList);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        bannerImage.setBannerAnimation(Transformer.Default);
        //设置是否为自动轮播，默认是“是”。
        bannerImage.isAutoPlay(false);
//        bannerImage.setOnBannerListener(this);
        bannerImage.start();
    }
}
