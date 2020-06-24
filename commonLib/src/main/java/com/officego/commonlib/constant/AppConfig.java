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

    public static String H5_OWNER_BUILDINGlIST = "";//房源管理
    public static String H5_OWNER_idify = "";//认证

    public static String RC_APPKEY = "";
    public static String RC_APPSECRET = "";

    @Override
    protected void initTest(Context context, String env) {
        APP_URL = "http://admin.officego.com.cn/";
//        APP_URL = "http://test.officego.com.cn/";
        H5_ABOUT_US = "http://test.officego.com.cn/lessee/aboutUs.html";
        H5_HELP_FEEDBACK = "http://test.officego.com.cn/lessee/opinion.html";
        H5_PRIVACY = "http://test.officego.com.cn/lessee/privacy.html";
        H5_REGISTER = "http://test.officego.com.cn/lessee/registerProtocol.html";
        H5_QA = "http://test.officego.com.cn/lessee/issueList.html";

        H5_OWNER_BUILDINGlIST = "http://test.officego.com.cn/owner/branchList.html";
        H5_OWNER_idify = "https://test.officego.com//owner/myHome.html";

        //融云IM test
        RC_APPKEY = "kj7swf8oknm02";
        RC_APPSECRET = "OF78PpILjjRk4";
    }

    @Override
    protected void initRelease(Context context, String env) {
        APP_URL = "http://admin.officego.com.cn/";
//        H5_ABOUT_US = "http://m.officego.com.cn/lessee/aboutUs.html";
//        H5_HELP_FEEDBACK = "http://m.officego.com.cn/lessee/opinion.html";
//        H5_PRIVACY = "http://m.officego.com.cn/lessee/privacy.html";
//        H5_REGISTER = "http://m.officego.com.cn/lessee/registerProtocol.html";
//        H5_QA = "http://m.officego.com.cn/lessee/issueList.html";
//
//        H5_OWNER_BUILDINGlIST = "http://m.officego.com.cn/owner/branchList.html";
//        H5_OWNER_idify = "https://m.officego.com//owner/myHome.html";
//
//        //融云IM release
//        RC_APPKEY = "qf3d5gbjq94mh";
//        RC_APPSECRET = "xtDkNCjJse";
        H5_ABOUT_US = "http://test.officego.com.cn/lessee/aboutUs.html";
        H5_HELP_FEEDBACK = "http://test.officego.com.cn/lessee/opinion.html";
        H5_PRIVACY = "http://test.officego.com.cn/lessee/privacy.html";
        H5_REGISTER = "http://test.officego.com.cn/lessee/registerProtocol.html";
        H5_QA = "http://test.officego.com.cn/lessee/issueList.html";

        H5_OWNER_BUILDINGlIST = "http://test.officego.com.cn/owner/branchList.html";
        H5_OWNER_idify = "https://test.officego.com//owner/myHome.html";

        //融云IM test
        RC_APPKEY = "kj7swf8oknm02";
        RC_APPSECRET = "OF78PpILjjRk4";
    }
}
