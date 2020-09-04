package com.officego.ui.home;

import android.content.Context;

import com.officego.ui.login.LoginActivity_;

/**
 * 租户当前未登录
 * 此时去登陆后，直接消失当前页面，不重新打开应用
 */
class LoginTenantUtils {
    LoginTenantUtils(Context context) {
        toLogin(context);
    }

    private void toLogin(Context context) {
        LoginActivity_.intent(context).isFinishCurrentView(true).start();
    }
}
