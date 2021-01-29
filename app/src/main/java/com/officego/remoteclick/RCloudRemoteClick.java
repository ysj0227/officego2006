package com.officego.remoteclick;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.log.LogCat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by shijie
 * Date 2021/1/21
 * 融云远程推送 通过bundle的点击事件
 **/
public class RCloudRemoteClick {
    public static final String TAG = "RCloudRemoteClick";

    public static RCloudRemoteClick getInstance() {
        return Singleton.INSTANCE;
    }

    private static final class Singleton {
        private static final RCloudRemoteClick INSTANCE = new RCloudRemoteClick();
    }

    /**
     * OPPO远程推送时在启动页判断bundle 然后进行跳转
     */
    public void pushClick(Activity context) {
        try {
            Bundle extras = context.getIntent().getExtras();
            if (extras != null && !TextUtils.isEmpty(extras.getString("rc"))) {
                String strRC = extras.getString("rc");
                String pushData = extras.getString("appData");
                LogCat.d(TAG, " oppo strRC=" + strRC);
                LogCat.d(TAG, " oppo pushData=" + pushData + "  context=" + context);
                GotoActivityUtils.MI_VIVO_PushClick(context, pushData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对华为获取参数方法
     */
    public void HWPushClick(Activity context) {
        Uri uri = context.getIntent().getData();
        if (uri != null) {
            if (TextUtils.equals("rong", uri.getScheme()) && uri.getQueryParameter("isFromPush") != null) {
                if (Objects.requireNonNull(uri.getQueryParameter("isFromPush")).equals("true")) {
                    String options = context.getIntent().getStringExtra("options"); // 获取 intent 里携带的附加数据
                    LogCat.d(TAG, "HW options:" + options);
                    try {
                        String pushData;
                        JSONObject jsonObject = new JSONObject(options);
                        if (jsonObject.has("appData")) {   // appData 对应的是客户端 sendMessage() 时的参数 pushData
                            pushData = jsonObject.getString("appData");
                            LogCat.d(TAG, "HW pushData=" + pushData);
                        } else {
                            pushData = "";
                        }
                        BaseNotification.newInstance().postNotificationName(CommonNotifications.RCloudPushData, pushData);
                    } catch (JSONException e) {
                        LogCat.d(TAG, "HW pushData JSONException");
                    }
                }
            }
        }
    }
}
