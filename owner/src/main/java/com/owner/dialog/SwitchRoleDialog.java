package com.owner.dialog;

import android.app.Activity;

import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.identity.SelectIdActivity_;

public class SwitchRoleDialog {
    public static void switchDialog(Activity activity) {
        CommonDialog dialog = new CommonDialog.Builder(activity)
                .setTitle("信息尚未提交，是否确认切换身份？")
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    SelectIdActivity_.intent(activity).start();
                    activity.finish();
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    //认证过程中，返回提示
    public static void identityBackDialog(Activity activity) {
        CommonDialog dialog = new CommonDialog.Builder(activity)
                .setTitle("确认离开吗？")
                .setMessage("信息尚未提交,是否确认离开？")
                .setConfirmButton(R.string.str_go_away, (dialog12, which) -> {
                    activity.finish();
//                    BaseNotification.newInstance().postNotificationName(CommonNotifications.ownerIdentityHandle, "ownerIdentityHandle");
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    /**
     * 认证提交成功
     */
    public static void submitIdentitySuccessDialog(Activity activity) {
        CommonDialog dialog = new CommonDialog.Builder(activity)
                .setTitle("提交成功")
                .setMessage("我们会在1-2个工作日完成审核")
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    //跳转房东个人中心
                    GotoActivityUtils.mainOwnerDefMainActivity(activity);
//                    BaseNotification.newInstance().postNotificationName(CommonNotifications.ownerIdentityHandle, "ownerIdentityHandle");
                    activity.finish();
                }).create();
        dialog.showWithOutTouchable(false);
        dialog.setCancelable(false);
    }
    /**
     * 认证提交成功
     */
    public static void submitIdentityTimeoutDialog(Activity activity) {
        CommonDialog dialog = new CommonDialog.Builder(activity)
                .setTitle("服务器响应超时，稍后再试")
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    //跳转房东个人中心
                    GotoActivityUtils.mainOwnerDefMainActivity(activity);
                    activity.finish();
                }).create();
        dialog.showWithOutTouchable(false);
        dialog.setCancelable(false);
    }
}
