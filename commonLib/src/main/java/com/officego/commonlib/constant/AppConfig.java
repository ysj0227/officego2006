package com.officego.commonlib.constant;

import android.content.Context;

import com.officego.commonlib.base.BaseConfig;

/**
 * app配置
 */
public class AppConfig extends BaseConfig {
    public static String APP_URL = "";//接口调用
    public static String APP_URL_MAIN = "";//数据连接

    public static String H5_ABOUT_US = "";//关于我们
    public static String H5_HELP_FEEDBACK = "";//帮助反馈
    public static String H5_HELP_FEEDBACK_OWNER = "";//业主帮助反馈
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

    public static String RC_APPKEY = "";//融云key
    public static String RC_APPSECRET = "";

    @Override
    protected void initTest(Context context, String env) {
        //URL
        APP_URL = "http://admin.officego.com.cn/";
//        APP_URL = "http://192.168.1.120/";
        APP_URL_MAIN = "http://test.officego.com.cn/";
        //个人中心
        H5_ABOUT_US = APP_URL_MAIN + "lessee/aboutUs.html";
        H5_HELP_FEEDBACK = APP_URL_MAIN + "lessee/opinion.html";
        H5_HELP_FEEDBACK_OWNER = APP_URL_MAIN + "owner/opinion.html";
        H5_PRIVACY = APP_URL_MAIN + "lessee/privacy.html";
        H5_REGISTER = APP_URL_MAIN + "lessee/registerProtocol.html";
        H5_QA = APP_URL_MAIN + "lessee/issueList.html";
        //楼盘网点管理
//        H5_OWNER_BUILDINGlIST = "http//172.16.4.12:8080/owner/houseList.html";
//        H5_OWNER_HOUSElIST = "http//172.16.4.12:8080/owner/branchList.html";
        H5_OWNER_BUILDINGlIST =APP_URL_MAIN + "owner/houseList.html";
        H5_OWNER_HOUSElIST = APP_URL_MAIN + "owner/branchList.html";
        //认证
        H5_OWNER_IDIFY = APP_URL_MAIN + "owner/myHome.html";
        H5_OWNER_IDIFY_PERSION = APP_URL_MAIN + "owner/attestationPersonage.html";
        H5_OWNER_IDIFY_COMPANY = APP_URL_MAIN + "owner/company.html";
        H5_OWNER_IDIFY_JOINTWORK = APP_URL_MAIN + "owner/company2.html";
        H5_STAFF_LIST = APP_URL_MAIN + "owner/staffList.html";  //员工管理
        //融云IM test
        RC_APPKEY = "kj7swf8oknm02";
        RC_APPSECRET = "OF78PpILjjRk4";
    }

    @Override
    protected void initRelease(Context context, String env) {
        //URL
        APP_URL = "https://api.officego.com/";
        APP_URL_MAIN = "https://m.officego.com/";
        //个人中心
        H5_ABOUT_US = APP_URL_MAIN + "lessee/aboutUs.html";
        H5_HELP_FEEDBACK = APP_URL_MAIN + "lessee/opinion.html";
        H5_HELP_FEEDBACK_OWNER = APP_URL_MAIN + "owner/opinion.html";
        H5_PRIVACY = APP_URL_MAIN + "lessee/privacy.html";
        H5_REGISTER = APP_URL_MAIN + "lessee/registerProtocol.html";
        H5_QA = APP_URL_MAIN + "lessee/issueList.html";
        //楼盘网点管理
        H5_OWNER_BUILDINGlIST = APP_URL_MAIN + "owner/houseList.html";
        H5_OWNER_HOUSElIST = APP_URL_MAIN + "owner/branchList.html";
        //认证
        H5_OWNER_IDIFY = APP_URL_MAIN + "owner/myHome.html";
        H5_OWNER_IDIFY_PERSION = APP_URL_MAIN + "owner/attestationPersonage.html";
        H5_OWNER_IDIFY_COMPANY = APP_URL_MAIN + "owner/company.html";
        H5_OWNER_IDIFY_JOINTWORK = APP_URL_MAIN + "owner/company2.html";
        H5_STAFF_LIST = APP_URL_MAIN + "owner/staffList.html";  //员工管理
        //融云IM release
        RC_APPKEY = "qf3d5gbjq94mh";
        RC_APPSECRET = "xtDkNCjJse";
    }
}
