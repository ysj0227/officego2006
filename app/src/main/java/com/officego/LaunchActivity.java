package com.officego;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.location.LocationUtils;
import com.officego.remoteclick.RCloudRemoteClick;
import com.officego.ui.dialog.ProtocolDialog;
import com.officego.ui.find.WantToFindActivity_;
import com.officego.ui.login.LoginActivity_;


@SuppressLint("Registered")
public class LaunchActivity extends BaseActivity
        implements ProtocolDialog.AgreeProtocolListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationUtils.getInstance().initLocation(context);
        PermissionUtils.getLocationPermission(this);
        LocationUtils.getInstance().startLocation();
        if (!isTaskRoot()) {
            finish();
            return;
        }
        StatusBarUtils.setStatusBarFullTransparent(this);
        if (TextUtils.isEmpty(SpUtils.getProtocol())) {
            new ProtocolDialog(context).setListener(this);
        } else {
            new Handler().postDelayed(this::gotoMainActivity, 500);
        }
        //神策
        SensorsTrack.sensorsLogin(SpUtils.getUserId());
        //融云推送
        remotePush();
    }

    private void remotePush() {
        //小米 vivo远程推送
        Uri uri = getIntent().getData();
        if (uri != null) {
            String pushData = uri.getQueryParameter("pushData");
            LogCat.e(TAG, "远程推送 启动页 pushData=" + pushData);
        }
        //1 OPPO远程推送点击 2华为（manifest没有配置华为时调用）
        RCloudRemoteClick.getInstance().pushClick(this);
        //RCloudRemoteClick.getInstance().HWPushClick(this);
    }

    private void gotoMainActivity() {
        if (TextUtils.isEmpty(SpUtils.getLead())) {
            LeadPagesActivity_.intent(context).start();
        } else {
            if (SpUtils.isShowWantFind()) {
                WantToFindActivity_.intent(context).start();  //我想找页面
            } else {
                if (TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRole())) {
                    LoginActivity_.intent(context).start();
                } else {
                    MainActivity_.intent(context).start();
                }
            }
        }
        finish();
    }

    @Override
    public void sureProtocol() {
        gotoMainActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtils.getInstance().destroyLocation();
    }

}
