package com.officego.commonlib.common.rongcloud.push;

import android.content.Context;
import android.os.Bundle;

import io.rong.push.PushManager;
import io.rong.push.PushType;
import io.rong.push.common.RLog;
import io.rong.push.platform.hms.HMSReceiver;

public class HUAWEIPushReceiver extends HMSReceiver {

    public void onToken(Context context, String token, Bundle bundle) {
        PushManager.getInstance().onReceiveToken(context, PushType.HUAWEI, token);
    }

    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        RLog.d("HMSReceiver", "onPushMsg");

        try {
            String content = new String(msg, "UTF-8");
            PushManager.getInstance().onPushRawData(context, PushType.HUAWEI, content);
        } catch (Exception var5) {
            RLog.e("HMSReceiver", "onPushMsg");
        }

        return false;
    }

    public void onPushState(Context context, boolean pushState) {
        RLog.e("HMSReceiver", "The current push status： " + (pushState ? "Connected" : "Disconnected"));
    }
}
