package com.officego.utils;

import android.content.Context;

import com.officego.R;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.LoadingDialog;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.dialog.CommonDialog;

import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.LoginSettings;

/**
 * Created by shijie
 * Date 2020/12/15
 * 极光授权登录
 **/
public class JPushAuthLoginRequest {

    public static final String TAG = "JPushAuthLoginRequest";
    private static final int LOGIN_SUCCESS = 6000;
    private static final int LOGIN_EXCEPTION = 6003;
    private static final int LOGIN_NETWORK_FAIL = 2016;
    private LoadingDialog loadingDialog;

    public static JPushAuthLoginRequest getInstance() {
        return Singleton.INSTANCE;
    }

    private static final class Singleton {
        private static final JPushAuthLoginRequest INSTANCE = new JPushAuthLoginRequest();
    }

    //sdk集成页面
    public void authLogin(Context mContext) {
        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(mContext);
        if (!verifyEnable) {
            CommonDialog dialog = new CommonDialog.Builder(mContext)
                    .setTitle("当前网络环境不支持一键登录\n请开启手机移动数据")
                    .setConfirmButton(R.string.str_confirm).create();
            dialog.showWithOutTouchable(true);
            return;
        }
        showLoadingDialog(mContext);
        LoginSettings settings = new LoginSettings();
        settings.setAutoFinish(true);//设置登录完成后是否自动关闭授权页
        settings.setTimeout(15 * 1000);//设置超时时间，单位毫秒。 合法范围（0，30000],范围以外默认设置为10000
        settings.setAuthPageEventListener(new AuthPageEventListener() {
            @Override
            public void onEvent(int cmd, String msg) {
                //LogCat.e(TAG, "cmd=" + cmd + "  msg=" + msg);
            }
        });
        JVerificationInterface.setCustomUIWithConfig(builder());
        JVerificationInterface.loginAuth(mContext, settings, (code, content, operator) -> {
            hideLoadingDialog();
            //LogCat.e(TAG, "code=" + code + "  content=" + content);
            if (code == LOGIN_SUCCESS) {
                BaseNotification.newInstance().postNotificationName(CommonNotifications.JPushSendPhone, content);
            } else if (code == LOGIN_NETWORK_FAIL) {
                ToastUtils.toastForShort(mContext, "请开启手机移动数据");
            } else if (code == LOGIN_EXCEPTION) {
                ToastUtils.toastForShort(mContext, "手机号获取失败");
            }
        });
    }

    //自定义ui
    public JVerifyUIConfig builder() {
        JVerifyUIConfig uiConfig = new JVerifyUIConfig.Builder()
                .setLogoImgPath("jpush_app_logo")
                .setLogoWidth(80)
                .setLogoHeight(80)
                .setLogoHidden(false)
                .setNavTextSize(20)
                .setNavColor(0xff46C3C2)
                .setNavTextColor(0xffffffff)
                .setNumberColor(0xff46C3C2)
                .setNumberSize(24)
                .setLogBtnHeight(50)
                .setLogBtnTextSize(18)
                .setAppPrivacyColor(0xFFBBBCC5, 0XFF46C3C2)
                .setPrivacyText("登录即同意《", "", "", "》并授权OfficeGo平台")
                .setPrivacyCheckboxHidden(false)
                .setPrivacyTextCenterGravity(true)
                .setSloganTextSize(14) //移动，联通，电信
                .setUncheckedImgPath("umcsdk_uncheck_image2")
                .setCheckedImgPath("umcsdk_check_image2")
                .setPrivacyCheckboxSize(14)
                .setPrivacyTextSize(13)
                .setPrivacyNavColor(0XFF46C3C2)
                .setPrivacyState(true)//是否选中协议
                .build();
        return uiConfig;
    }

    /**
     * 显示加载框
     */
    public void showLoadingDialog(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setLoadingContent(null);
            loadingDialog.show();
        }
    }

    /**
     * 关闭加载框
     */
    public void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.setCancelable(true);
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
