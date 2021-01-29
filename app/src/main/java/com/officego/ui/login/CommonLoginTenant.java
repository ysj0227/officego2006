package com.officego.ui.login;

import android.content.Context;

/**
 * 租户当前未登录
 * 此时去登陆后，直接消失当前页面，不重新打开应用
 */
public class CommonLoginTenant {
    public CommonLoginTenant(Context context) {
        toLogin(context);
    }

    private void toLogin(Context context) {
        LoginActivity_.intent(context).isFinishCurrentView(true).start();
    }
}
