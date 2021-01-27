package com.owner.mine;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;

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

    @ViewById(resName = "sil_mobile")
    SettingItemLayout silMobile;
    @ViewById(resName = "sil_version_update")
    SettingItemLayout silVersionUpdate;

    @SuppressLint("SetTextI18n")
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        silVersionUpdate.setLeftToArrowText("v" + CommonHelper.getAppVersionName(context));
        String mobile = SpUtils.getPhoneNum();
        if (!TextUtils.isEmpty(mobile) && mobile.length() == 11) {
            String phoneNumber = mobile.substring(0, 3) + "****" + mobile.substring(7);
            silMobile.setLeftToArrowText(phoneNumber);
        }
    }

    @Click(resName = "sil_mobile")
    void modifyMobileClick() {
        ModifyMobileActivity_.intent(context).start();
    }

    @Click(resName = "sil_version_update")
    void versionUpdateClick() {
        new VersionDialog(context, true);
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
}
