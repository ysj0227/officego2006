package com.officego.ui.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.VersionBean;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.update.AppUpdate;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.mine.ModifyMobileActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/5/20.
 * Descriptions: setting
 **/
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.mine_activity_setting)
public class MineSettingActivity extends BaseActivity {
    @ViewById(R.id.tv_version)
    TextView tvVersion;
    @ViewById(R.id.tv_mobile)
    TextView tvMobile;

    @SuppressLint("SetTextI18n")
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        tvVersion.setText("v" + CommonHelper.getAppVersionName(context));
        String mobile = SpUtils.getPhoneNum();
        if (!TextUtils.isEmpty(mobile) && mobile.length() == 11) {
            String phoneNumber = mobile.substring(0, 3) + "****" + mobile.substring(7);
            tvMobile.setText(phoneNumber);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            CommonDialog dialog = new CommonDialog.Builder(context)
                    .setMessage("账号已退出，请重新登录")
                    .setConfirmButton(com.officego.commonlib.R.string.str_login, (dialog12, which) -> {
                        GotoActivityUtils.gotoLoginActivity(context);
                        dialog12.dismiss();
                    }).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
        }
    }

    @Click(R.id.rl_mobile)
    void modifyMobileClick() {
        ModifyMobileActivity_.intent(context).start();
    }

    @Click(R.id.rl_version_update)
    void versionUpdateClick() {
        updateVersion(CommonHelper.getAppVersionName(context));
    }


    @Click(R.id.btn_logout)
    void logoutClick() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(R.string.str_are_you_sure_to_logout)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    BaseNotification.newInstance().postNotificationName(
                            CommonNotifications.loginOut, "loginOut");
                    SpUtils.clearLoginInfo();
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    private void updateDialog(Activity context, String dec, String url) {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle("发现新版本")
                .setMessage(dec)
                .setConfirmButton(R.string.str_update, (dialog12, which) -> AppUpdate.versionUpdate(context, url))
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
        dialog.setCancelable(false);
    }

    private void updateVersion(String versionName) {
        showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().updateVersion(versionName, new RetrofitCallback<VersionBean>() {
            @Override
            public void onSuccess(int code, String msg, VersionBean data) {
                hideLoadingDialog();
                updateDialog((Activity) context, data.getDesc(), data.getUploadUrl());
            }

            @Override
            public void onFail(int code, String msg, VersionBean data) {
                hideLoadingDialog();
                if (code == Constants.ERROR_CODE_5008) {
                    shortTip(R.string.tip_current_newest_version);
                }
            }
        });
    }
}
