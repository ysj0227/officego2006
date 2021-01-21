package com.officego;

import android.annotation.SuppressLint;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.rongcloud.remoteclick.RCloudRemoteClick;
import com.officego.commonlib.utils.StatusBarUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by shijie
 * Date 2021/1/21
 **/
@SuppressLint("NonConstantResourceId")
@EActivity(R.layout.push_activity_details)
public class PushRemoteDetailActivity extends BaseActivity {

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        //华为远程推送点击
        RCloudRemoteClick.getInstance().HWPushClick(this);
    }
}
