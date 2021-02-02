package com.officego.ui.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.dialog.ExitAppDialog;
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
    @ViewById(R.id.sil_mobile)
    SettingItemLayout silMobile;
    @ViewById(R.id.sil_version_update)
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

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new ExitAppDialog(this);
        }
    }

    @Click(R.id.sil_mobile)
    void modifyMobileClick() {
        ModifyMobileActivity_.intent(context).start();
    }

    @Click(R.id.sil_version_update)
    void versionUpdateClick() {
        new VersionDialog(context, true);
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
}
