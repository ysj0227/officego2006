package com.officego.commonlib.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.officego.commonlib.utils.log.LogCat;

/**
 * Created by YangShiJie
 * Data 2020/6/13.
 * Descriptions:
 **/
public class MyBroadcastReceiver extends BroadcastReceiver {
    private Activity activity;
//    public static CommonDialog receiverDialog;

    public MyBroadcastReceiver(Activity context) {
        this.activity = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        LogCat.d("TAG", "1111100000 MyBroadcastReceiver");
//        if (receiverDialog != null && receiverDialog.isShowing()) {
//            receiverDialog.dismiss();
//            receiverDialog = null;
//        }
//        receiverDialog = new CommonDialog.Builder(context)
//                .setTitle("当前暂未登录，请登录后使用相关功能")
//                .setConfirmButton("去登录", (dialog12, which) -> {
//                    gotoLoginActivity(context);
//                    receiverDialog.dismiss();
//                    receiverDialog = null;
//                }).create();
//        receiverDialog.setCancelable(true);//禁用系统返回
//        receiverDialog.showWithOutTouchable(false);
        gotoLoginActivity(context);
    }

    private void gotoLoginActivity(Context context) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.login.LoginActivity_");
        Intent intent = new Intent();
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }
}
