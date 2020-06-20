package com.officego.commonlib.constant;

import android.content.Context;

import com.officego.commonlib.base.BaseConfig;

/**
 * 租户配置
 */
public class AppConfig extends BaseConfig {

    public static String APP_URL = "";

    public static String H5_ABOUT_US = "";
    public static String H5_HELP_FEEDBACK = "";
    public static String H5_PRIVACY = "";//隐私协议
    public static String H5_REGISTER = "";//注册协议
    public static String H5_QA = "";//常见问题

    @Override
    protected void initTest(Context context, String env) {
        APP_URL = "http://admin.officego.com.cn/";

        H5_ABOUT_US = "http://test.officego.com.cn/lessee/aboutUs.html";
        H5_HELP_FEEDBACK = "http://test.officego.com.cn/lessee/opinion.html";
//        H5_HELP_FEEDBACK = "http://test.officego.com.cn/owner/opinion.html";
        H5_PRIVACY = "http://test.officego.com.cn/lessee/privacy.html";
        H5_REGISTER = "http://test.officego.com.cn/lessee/registerProtocol.html";
        H5_QA = "http://test.officego.com.cn/lessee/issueList.html";
    }

    @Override
    protected void initRelease(Context context, String env) {
        APP_URL = "http://admin.officego.com.cn/";

        H5_ABOUT_US = "http://test.officego.com.cn/lessee/aboutUs.html";
        H5_HELP_FEEDBACK = "http://test.officego.com.cn/lessee/opinion.html";
        H5_PRIVACY = "http://test.officego.com.cn/lessee/privacy.html";
        H5_REGISTER = "http://test.officego.com.cn/lessee/registerProtocol.html";
        H5_QA = "http://test.officego.com.cn/lessee/issueList.html";
    }
}
