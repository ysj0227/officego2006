package com.owner.dialog;

import android.app.Activity;

import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;

public class ExitConfirmDialog {
    private Activity context;

    public ExitConfirmDialog(Activity context) {
        this.context = context;
        exitDialog();
    }
    private void exitDialog(){
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle("是否退出当前编辑页面？")
                .setCancelButton(R.string.sm_cancel)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    dialog12.dismiss();
                    context.finish();
                }).create();
        dialog.showWithOutTouchable(false);
    }
}
