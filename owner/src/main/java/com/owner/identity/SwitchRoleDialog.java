package com.owner.identity;

import android.app.Activity;

import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;

public class SwitchRoleDialog {
    public static void switchDialog(Activity activity) {
        CommonDialog dialog = new CommonDialog.Builder(activity)
                .setTitle("信息尚未提交，是否确认切换身份吗？")
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                   activity.finish();
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }
}
