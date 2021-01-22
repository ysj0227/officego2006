package com.officego.commonlib.common.rongcloud.remoteclick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.officego.commonlib.utils.log.LogCat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.rong.imlib.model.Conversation;

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
                String appData = extras.getString("appData");
                JSONObject rc = new JSONObject(strRC);
                Conversation.ConversationType conversationType =
                        Conversation.ConversationType.setValue(rc.getInt("conversationType"));
                String targetId = rc.getString("fromUserId");
                LogCat.e(TAG, " oppo strRC=" + strRC);
                LogCat.e(TAG, " oppo pushData=" + appData);
                //TODO
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
                    LogCat.e(TAG, "HW options:" + options);
                    try {
                        JSONObject jsonObject = new JSONObject(options);
                        if (jsonObject.has("appData")) {   // appData 对应的是客户端 sendMessage() 时的参数 pushData
                            String pushData = jsonObject.getString("appData");
                            LogCat.e(TAG, "HW pushData=" + pushData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 小米 vivo远程推送时在启动页判断bundle 然后进行跳转
     */
    public void MI_VIVO_PushClick(Context context, String pushData) {
        LogCat.e(TAG, "小米 vivo远程 pushData=" + pushData);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("url://officego/app?pushData=" + pushData));
        context.startActivity(intent);
    }

}
