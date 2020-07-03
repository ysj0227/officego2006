package com.officego.commonlib.update;

import android.app.Activity;
import android.content.Context;

import com.officego.commonlib.R;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.VersionBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.view.dialog.CommonDialog;

/**
 * Created by YangShiJie
 * Data 2020/5/14.
 * Descriptions:
 **/
public class VersionDialog {

    public VersionDialog(Context context) {
        serviceDialog(context);
    }

    public void serviceDialog(Context context) {
        updateVersion((Activity) context);
//        updateDialog((Activity) context, false, "ddd", "ddd");
    }

    private void updateDialog(Activity context, boolean isForce, String dec, String url) {
//        if (!TextUtils.isEmpty(SpUtils.getCancelUpdate())) {
//            return;
//        }
        if (isForce) {
            CommonDialog dialog = new CommonDialog.Builder(context)
                    .setTitle("发现新版本")
                    .setMessage(dec)
                    .setConfirmButton(R.string.str_update, (dialog12, which) -> AppUpdate.versionUpdate(context, url)).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
        } else {
            CommonDialog dialog = new CommonDialog.Builder(context)
                    .setTitle(dec)
                    .setConfirmButton(R.string.str_update, (dialog12, which) -> AppUpdate.versionUpdate(context, url))
                    .setCancelButton(R.string.sm_cancel, (dialog1, which) -> {
//                        SpUtils.saveCancelUpdate();
                        dialog1.dismiss();
                    }).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
        }
    }

    private void updateVersion(Activity context) {
        OfficegoApi.getInstance().updateVersion(
                CommonHelper.getAppVersionName(context), new RetrofitCallback<VersionBean>() {
                    @Override
                    public void onSuccess(int code, String msg, VersionBean data) {
                        updateDialog(context, data.isForce(), data.getDesc(), data.getUploadUrl());
                    }

                    @Override
                    public void onFail(int code, String msg, VersionBean data) {
                    }
                });
    }

}
