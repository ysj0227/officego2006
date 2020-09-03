package com.officego.ui.home;

import android.content.Context;

import com.officego.ui.login.LoginActivity_;

/**
 * 租户
 * 当前未登录
 */
class LoginTenantUtils {
    LoginTenantUtils(Context context) {
        toLogin(context);
    }

    private void toLogin(Context context) {
        LoginActivity_.intent(context).isFinishCurrentView(true).start();
    }
}
