package com.officego.commonlib.constant;

import android.content.Context;

import com.officego.commonlib.base.BaseConfig;

/**
 * 租户配置
 */
public class AppConfig extends BaseConfig {

    public static String APP_URL = "";
    public static String APP_SHARE_URL = "";

    public static String H5_ROLE = "";
    public static String H5_ABOUT_US = "";
    public static String H5_HELP_FEEDBACK = "";
    public static String H5_PRIVACY = "";//隐私协议
    public static String H5_REGISTER = "";//注册协议
    public static String H5_QA = "";//常见问题
    public static String H5_STAFF_LIST = "";//员工管理

    public static String H5_OWNER_BUILDINGlIST = "";//房源管理
    public static String H5_OWNER_HOUSElIST = "";// 网点管理
    public static String H5_OWNER_IDIFY = "";//认证
    public static String H5_OWNER_IDIFY_PERSION = "";//个人认证
    public static String H5_OWNER_IDIFY_COMPANY = "";//企业认证
    public static String H5_OWNER_IDIFY_JOINTWORK = "";//网点认证

    public static String RC_APPKEY = "";
    public static String RC_APPSECRET = "";

    @Override
    protected void initTest(Context context, String env) {
        APP_URL = "http://admin.officego.com.cn/";
        APP_SHARE_URL = "http://test.officego.com.cn/";
        H5_ROLE = "http://test.officego.com.cn/lessee/aboutUs.html";
        H5_ABOUT_US = "http://test.officego.com.cn/lessee/aboutUs.html";
        H5_HELP_FEEDBACK = "http://test.officego.com.cn/owner/opinion.html";
        H5_PRIVACY = "http://test.officego.com.cn/lessee/privacy.html";
        H5_REGISTER = "http://test.officego.com.cn/lessee/registerProtocol.html";
        H5_QA = "http://test.officego.com.cn/lessee/issueList.html";
        //楼盘网点管理
        H5_OWNER_BUILDINGlIST = "http://test.officego.com.cn/owner/houseList.html";
        H5_OWNER_HOUSElIST = "http://test.officego.com.cn/owner/branchList.html";
        //认证
        H5_OWNER_IDIFY = "http://test.officego.com.cn/owner/myHome.html";
        H5_OWNER_IDIFY_PERSION = "http://test.officego.com.cn/owner/attestationPersonage.html";
        H5_OWNER_IDIFY_COMPANY = "http://test.officego.com.cn/owner/company.html";
        H5_OWNER_IDIFY_JOINTWORK = "http://test.officego.com.cn/owner/company2.html";
        //员工管理
        H5_STAFF_LIST = "http://test.officego.com.cn/owner/staffList.html";
        //融云IM test
        RC_APPKEY = "kj7swf8oknm02";
        RC_APPSECRET = "OF78PpILjjRk4";
    }

    @Override
    protected void initRelease(Context context, String env) {
        APP_URL = "http://admin.officego.com.cn/";
        APP_SHARE_URL = "http://test.officego.com.cn/";
        //融云IM release
//        RC_APPKEY = "qf3d5gbjq94mh";
//        RC_APPSECRET = "xtDkNCjJse";
        H5_ROLE = "http://test.officego.com.cn/lessee/aboutUs.html";
        H5_ABOUT_US = "http://test.officego.com.cn/lessee/aboutUs.html";
        H5_HELP_FEEDBACK = "http://test.officego.com.cn/owner/opinion.html";
        H5_PRIVACY = "http://test.officego.com.cn/lessee/privacy.html";
        H5_REGISTER = "http://test.officego.com.cn/lessee/registerProtocol.html";
        H5_QA = "http://test.officego.com.cn/lessee/issueList.html";
        //楼盘网点管理
        H5_OWNER_BUILDINGlIST = "http://test.officego.com.cn/owner/houseList.html";
        H5_OWNER_HOUSElIST = "http://test.officego.com.cn/owner/branchList.html";
        //认证
        H5_OWNER_IDIFY = "http://test.officego.com.cn/owner/myHome.html";
        H5_OWNER_IDIFY_PERSION = "http://test.officego.com.cn/owner/attestationPersonage.html";
        H5_OWNER_IDIFY_COMPANY = "http://test.officego.com.cn/owner/company.html";
        H5_OWNER_IDIFY_JOINTWORK = "http://test.officego.com.cn/owner/company2.html";
        //员工管理
        H5_STAFF_LIST = "http://test.officego.com.cn/owner/staffList.html";

        //融云IM test
        RC_APPKEY = "kj7swf8oknm02";
        RC_APPSECRET = "OF78PpILjjRk4";
    }
}
