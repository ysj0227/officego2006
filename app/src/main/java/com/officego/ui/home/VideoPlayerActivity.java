package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.CommonHelper;

public class VideoPlayerActivity extends BaseActivity {
    private IVideoView mVideoView;
    String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    LinearLayout.LayoutParams params;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_activity);
        mVideoView = findViewById(R.id.video_view);
        params= (LinearLayout.LayoutParams) mVideoView.getLayoutParams();

        initNetVideo();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
            }
        });

    }

    //播放网络视频
    private void initNetVideo() {
        //设置有进度条可以拖动快进
        MediaController localMediaController = new MediaController(this);
        mVideoView.setMediaController(localMediaController);
//        String url = "https://flv2.bn.netease.com/videolib1/1811/26/OqJAZ893T/HD/OqJAZ893T-mobile.mp4";
        mVideoView.setVideoPath(videoUrl);
        mVideoView.start();
    }

    /**
     * 监听屏幕方向改变
     * @param newConfig
     * 配置文件配置了configChanges才会走次此回调
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //配置文件中设置 android:configChanges="orientation|screenSize|keyboardHidden" 不然横竖屏切换的时候重新创建又重新播放
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mVideoView.setLayoutParams(params);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);//显示顶部状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    CommonHelper.dp2px(this,300));
            mVideoView.setLayoutParams(params);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏不 显示顶部状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        System.out.println("======onConfigurationChanged===");
    }

}
