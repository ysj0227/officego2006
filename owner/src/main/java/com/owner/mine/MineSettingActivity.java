package com.owner.mine;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.VersionBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.rpc.OfficegoApi;
import com.owner.update.AppUpdate;
import com.owner.utils.GotoActivityUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by YangShiJie
 * Data 2020/5/20.
 * Descriptions: setting
 **/
@SuppressLint("Registered")
@EActivity(resName = "mine_owner_activity_setting")
public class MineSettingActivity extends BaseActivity {

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this, StatusBarUtils.TYPE_DARK);
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
        //业主切换租户
        switchId("0");
    }

    @Click(resName = "btn_logout")
    void logoutClick() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(R.string.str_are_you_sure_to_logout)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    SpUtils.clearLoginInfo();
                    //跳转登录
                    GotoActivityUtils.loginClearActivity(context);
//                    Intent intent = getIntent();
//                    setResult(RESULT_OK, intent);
//                    finish();
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    private void updateDialog(Activity context, String title, String url) {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(title)
                .setConfirmButton(R.string.str_update, (dialog12, which) -> AppUpdate.versionUpdate(context, url))
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    private void updateVersion(String versionName) {
        showLoadingDialog();
        OfficegoApi.getInstance().updateVersion(versionName, new RetrofitCallback<VersionBean>() {
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

    //用户身份标：0租户，1户主
    private void switchId(String role) {
        showLoadingDialog();
        OfficegoApi.getInstance().switchId(role, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                LogCat.e(TAG, "switchId onSuccess code");
                hideLoadingDialog();
                SpUtils.saveLoginInfo(data, SpUtils.getPhoneNum());
                SpUtils.saveRole(role);
                //跳转租户首页
                GotoActivityUtils.mainActivity(context);
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                LogCat.e(TAG, "switchId onFail code=" + code + "  msg=" + msg);
                hideLoadingDialog();
                if (code == Constants.DEFAULT_ERROR_CODE || code == Constants.ERROR_CODE_5009) {
                    shortTip(msg);
                    SpUtils.clearLoginInfo();
                    GotoActivityUtils.selectedIdActivity(context);
                } else {
                    shortTip("切换角色失败");
                }
            }
        });
    }
}
