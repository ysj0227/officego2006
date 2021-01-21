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
import io.rong.push.RongPushClient;

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
    public void OPPOPushClick(Activity context) {
        try {
            Bundle extras = context.getIntent().getExtras();
            if (extras != null && !TextUtils.isEmpty(extras.getString("rc"))) {
                String strRC = extras.getString("rc");
                String content = extras.getString("content");
                JSONObject rc = new JSONObject(strRC);
                Conversation.ConversationType conversationType =
                        Conversation.ConversationType.setValue(rc.getInt("conversationType"));
                String targetId = rc.getString("fromUserId");
                LogCat.e(TAG, " oppo strRC=" + strRC);
                LogCat.e(TAG, " oppo content=" + content);
                gotoActivity(context, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对华为获取参数方法
     */
    public void HWPushClick(Activity context) {
        Intent intent = context.getIntent();
        if (Objects.equals(Objects.requireNonNull(intent.getData()).getScheme(), "rong") &&
                intent.getData().getQueryParameter("isFromPush") != null) {
            if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
                String options = context.getIntent().getStringExtra("options"); // 获取 intent 里携带的附加数据
                LogCat.e(TAG, "HW options:" + options);
                try {
                    JSONObject jsonObject = new JSONObject(options);
                    if (jsonObject.has("appData")) {   // appData 对应的是客户端 sendMessage() 时的参数 pushData
                        LogCat.e(TAG, "pushData:" + jsonObject.getString("appData"));
                    }
                    if (jsonObject.has("rc")) {
                        JSONObject rc = jsonObject.getJSONObject("rc");
                        LogCat.e(TAG, "rc:" + rc);
                        String targetId = rc.getString("tId"); // 该推送通知对应的目标 id.
                        String pushId = rc.getString("id");  // 开发者后台使用广播推送功能发出的推送通知，会有该字段，代表该推送的唯一 id， 需要调用下面接口，上传用户打开事件。
                        if (!TextUtils.isEmpty(pushId)) {
                            RongPushClient.recordNotificationEvent(pushId); // 上传用户打开事件，以便进行推送打开率的统计。
                            LogCat.e(TAG, "pushId:" + pushId);
                        }
                        if (rc.has("ext") && rc.getJSONObject("ext") != null) {
                            String ext = rc.getJSONObject("ext").toString(); // 使用开发者后台的广播推送功能时，填充的自定义键值对。
                            LogCat.e(TAG, "ext:" + ext);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //跳转页面
            }
        }
    }

    /**
     * 小米 vivo远程推送时在启动页判断bundle 然后进行跳转
     */
    public void MI_VIVO_PushClick(Context context, String pushData) {
        gotoActivity(context, pushData);
    }

    /**
     * 远程推送的点击事件 针对小米和vivo手机
     */
    private void gotoActivity(Context context, String pushData) {
        String uriString = "rong://com.officego/push_message?";
        Intent intent = new Intent();
        intent.setData(Uri.parse(uriString));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//必加
        context.startActivity(intent);
    }
}
