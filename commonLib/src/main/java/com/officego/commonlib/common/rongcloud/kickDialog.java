package com.officego.commonlib.common.rongcloud;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.officego.commonlib.R;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.view.dialog.CommonDialog;

/**
 * Created by YangShiJie
 * Data 2020/6/30.
 * Descriptions: 踢出跳转登录
 **/
public class kickDialog {

    public kickDialog(Context context) {
        dialog(context);
    }

    private void dialog(Context context) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            CommonDialog dialog = new CommonDialog.Builder(context)
                    .setTitle("你的账号已在其他设备上登录，是否重新登录")
                    .setMessage("如果不是你的操作，请尽快修改密码")
                    .setCancelButton(R.string.sm_cancel, (dialog12, which) -> {
                        SpUtils.clearLoginInfo();
                        GotoActivityUtils.loginClearActivity(context);
                        dialog12.dismiss();
                    })
                    .setConfirmButton(R.string.str_login, (dialog12, which) -> {
                        //重连
                        new RCloudConnectUtils();
                        dialog12.dismiss();
                    }).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
        });

    }
}
