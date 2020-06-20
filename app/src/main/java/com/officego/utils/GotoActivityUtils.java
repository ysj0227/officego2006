package com.officego.utils;

import android.content.Context;
import android.content.Intent;

import com.officego.MainActivity_;
import com.officego.ui.login.LoginActivity_;

/**
 * Created by YangShiJie
 * Data 2020/5/29.
 * Descriptions:
 **/
public class GotoActivityUtils {

    /**
     * 跳转首页
     */
    public static void gotoMainActivity(Context context) {
        MainActivity_.intent(context).start();
    }

    /**
     * 跳转登录
     */
    public static void gotoLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 回到桌面
     */
    public static void gotoHome(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }
}
