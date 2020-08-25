package com.officego.ui.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.officego.MainOwnerActivity_;
import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.VersionBean;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rongcloud.ConnectRongCloudUtils;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.update.AppUpdate;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.rpc.OfficegoApi;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/5/20.
 * Descriptions: setting
 **/
@SuppressLint("Registered")
@EActivity(R.layout.mine_activity_setting)
public class MineSettingActivity extends BaseActivity {
    @ViewById(R.id.tv_version)
    TextView tvVersion;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        tvVersion.setText("v" + CommonHelper.getAppVersionName(context));
    }

    @Click(R.id.rl_version_update)
    void versionUpdateClick() {
        updateVersion(CommonHelper.getAppVersionName(context));
    }

    @Click(R.id.rl_switch_id)
    void switchIdClick() {
        switchDialog();
    }

    private void switchDialog() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(R.string.are_you_sure_switch_owner)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    switchId(Constants.TYPE_OWNER);
                    //神策
                    SensorsTrack.tenantToOwner();
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
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
                LogCat.e(TAG, "updateVersion onFail code=" + code + "  msg=" + msg);
                hideLoadingDialog();
                if (code == Constants.ERROR_CODE_5008) {
                    shortTip(R.string.tip_current_newest_version);
                }
            }
        });
    }

    //租户端---用户身份标：0租户，1户主
    private void switchId(String role) {
        showLoadingDialog();
        OfficegoApi.getInstance().switchId(role, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                hideLoadingDialog();
                SpUtils.saveLoginInfo(data, SpUtils.getPhoneNum());
                SpUtils.saveRole(String.valueOf(data.getRid()));
                new ConnectRongCloudUtils();//连接融云
                if (TextUtils.equals(Constants.TYPE_TENANT, String.valueOf(data.getRid()))) {
                    GotoActivityUtils.mainActivity(context); //跳转租户首页
                } else if (TextUtils.equals(Constants.TYPE_OWNER, String.valueOf(data.getRid()))) {
                    MainOwnerActivity_.intent(context).start(); //租户切换房东
                }
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                hideLoadingDialog();
                if (code == Constants.ERROR_CODE_5009) {
                    shortTip(msg);
                    SpUtils.clearLoginInfo();
                    GotoActivityUtils.loginClearActivity(context, false);
                }
            }
        });
    }
}
