package com.officego.utils.video;

import android.content.Context;
import android.view.ViewGroup;

import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.view.IVideoPlayer;

/**
 * Created by shijie
 * Date 2021/1/28
 * 视频参数设置
 **/
public class IjkVideoUtils {
    /**
     * @param context      context
     * @param iVideoPlayer iVideoPlayer
     * @param width        视频的宽
     * @param height       视频的高
     */
    public static void setVideoLayout(Context context, IVideoPlayer iVideoPlayer, int width, int height) {
        if (iVideoPlayer != null) {
            ViewGroup.LayoutParams params = iVideoPlayer.getLayoutParams();
            int screenWidth = CommonHelper.getScreenWidth(context);
            int videoWidth, videoHeight;
            if (width - height > 10) {
                videoWidth = screenWidth;
                videoHeight = (int) (screenWidth / CommonHelper.digits(width, height));
            } else if (height - width > 10) {
                videoWidth = (int) (screenWidth / CommonHelper.digits(height, width));
                videoHeight = screenWidth;
            } else {
                videoWidth = videoHeight = screenWidth;
            }
            params.width = videoWidth;
            params.height = videoHeight;
            iVideoPlayer.setLayoutParams(params);
        }
    }
}
