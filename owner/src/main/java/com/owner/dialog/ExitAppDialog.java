package com.owner.dialog;

import android.app.Activity;

import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.view.dialog.CommonDialog;

public class ExitAppDialog {
    private Activity context;

    public ExitAppDialog(Activity context) {
        this.context = context;
        exitDialog();
    }
    private void exitDialog(){
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setMessage("账号未登录，请登录")
                .setConfirmButton(com.officego.commonlib.R.string.str_login, (dialog12, which) -> {
                    GotoActivityUtils.gotoLoginActivity(context);
                    dialog12.dismiss();
                }).create();
        dialog.showWithOutTouchable(false);
        dialog.setCancelable(false);
    }
}
