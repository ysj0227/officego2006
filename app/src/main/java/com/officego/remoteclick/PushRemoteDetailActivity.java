package com.officego.remoteclick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.officego.LaunchActivity;
import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.ui.home.BannerToActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shijie
 * Date 2021/1/21
 **/
@SuppressLint("NonConstantResourceId")
public class PushRemoteDetailActivity extends BaseActivity {
    private final int REQUEST_CODE = 10000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_activity_details);
        StatusBarUtils.setStatusBarColor(this);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("pushData")) {
            String pushData = intent.getStringExtra("pushData");
            LogCat.e(TAG, "pushData=" + pushData);
            if (!TextUtils.isEmpty(pushData)) {
                toDetailsActivity(pushData);
            }
        } else {
            //华为
            RCloudRemoteClick.getInstance().HWPushClick(this);
        }
    }

    /**
     * 通过参数跳转到指定详情页面
     */
    private void toDetailsActivity(String json) {
        if (TextUtils.isEmpty(json)) {
            launchActivity();
            return;
        }
        try {
            JSONObject object = new JSONObject(json);
            if (object.has("type") && object.has("pageType")) {
                int type = object.getInt("type");
                int pageType = object.getInt("pageType");
                int pageId = object.getInt("pageId");
                String wUrl = object.getString("wurl");
                BannerToActivity.toPushActivity(context, REQUEST_CODE, type, pageType, pageId, wUrl);
            } else {
                launchActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchActivity() {
        Intent intent = new Intent(context, LaunchActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode) {
            launchActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        launchActivity();
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.RCloudPushData};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (id == CommonNotifications.RCloudPushData) {
            String pushData = (String) args[0];
            toDetailsActivity(pushData);
        }
    }
}
