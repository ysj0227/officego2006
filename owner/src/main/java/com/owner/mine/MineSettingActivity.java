package com.owner.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.widget.TextView;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.VersionBean;
import com.officego.commonlib.common.rongcloud.ConnectRongCloudUtils;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.update.AppUpdate;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.rpc.OfficegoApi;

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
@EActivity(resName = "mine_owner_activity_setting")
public class MineSettingActivity extends BaseActivity {
    @ViewById(resName = "tv_mobile")
    TextView tvMobile;
    @ViewById(resName = "tv_version")
    TextView tvVersion;


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

    @Click(resName = "rl_mobile")
    void modifyMobileClick() {
        ModifyMobileActivity_.intent(context).start();
    }

    @Click(resName = "rl_wx")
    void modifyWechatClick() {
        ModifyWechatActivity_.intent(context).start();
    }

    @Click(resName = "rl_version_update")
    void versionUpdateClick() {
        updateVersion(CommonHelper.getAppVersionName(context));
    }

    @Click(resName = "rl_switch_id")
    void switchIdClick() {
        //房东切换租户
        switchDialog();
    }

    private void switchDialog() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(R.string.are_you_sure_switch_tenant)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    switchId(Constants.TYPE_TENANT);
                    //神策
                    SensorsTrack.ownerToTenant();
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    @Click(resName = "btn_logout")
    void logoutClick() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(R.string.str_are_you_sure_to_logout)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    SpUtils.clearLoginInfo();
                    //跳转登录
                    GotoActivityUtils.loginClearActivity(context, true);
                    finish();
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    private void updateDialog(Activity context, String title, String url) {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle("发现新版本")
                .setMessage(title)
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

    //房东端--- 用户身份标：0租户，1户主
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
                    GotoActivityUtils.mainOwnerActivity(context); //租户切换房东
                }
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                hideLoadingDialog();
                if (code == Constants.ERROR_CODE_5009) {
                    shortTip(msg);
                    SpUtils.clearLoginInfo();
                    GotoActivityUtils.loginClearActivity(context, true);
                }
            }
        });
    }
}
