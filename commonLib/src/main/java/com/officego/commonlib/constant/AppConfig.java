package com.officego.commonlib.constant;

import android.content.Context;

import com.officego.commonlib.base.BaseConfig;

/**
 * app配置
 */
public class AppConfig extends BaseConfig {
    public static String APP_URL = "";//接口调用
    public static String APP_URL_MAIN = "";//数据连接
    public static String WEB_URL_SCAN_LOGIN = "";//app扫码web登录

    public static String H5_ABOUT_US = "";//关于我们
    public static String H5_HELP_FEEDBACK = "";//帮助反馈
    public static String H5_HELP_FEEDBACK_OWNER = "";//房东帮助反馈
    public static String H5_PRIVACY = "";//隐私协议
    public static String H5_REGISTER = "";//注册协议
    public static String H5_QA = "";//常见问题
    public static String H5_STAFF_LIST = "";//员工管理

    public static String H5_OWNER_BUILDINGlIST = "";//房源管理
    public static String H5_OWNER_HOUSElIST = "";// 网点管理
    //融云key
    public static String RC_APPKEY = "";
    public static String RC_APPSECRET = "";
    //bugly
    public static String BUGLY_ID = "";
    //小米推送
    public static final String MI_APP_ID = "2882303761518466472";
    public static final String MI_APP_KEY = "5901846688472";
    //OPPO推送
    public static final String OPPO_APP_KEY = "114e9b66ee5d4bc394d5267f30c00034";
    public static final String OPPO_APP_SECRET = "acb2bbe32bf04af38c961229fb4b4b13";
    //Vivo推送
    public static final String VIVO_APP_ID = "103930536";
    public static final String VIVO_APP_KEY = "6ae46b0df31681793fc065eb57ba9c03";
    //神策数据
    public static String SA_SERVER_URL = "";

    //测试
    @Override
    protected void initTest(Context context, String env) {
        //预发环境
//        APP_URL = "http://admin.officego.com.cn/";
//        APP_URL_MAIN = "http://test.officego.com.cn/";
        //开发环境
        APP_URL = "http://debug.officego.com.cn/";
        APP_URL_MAIN = "http://test1.officego.com.cn/";
        //web扫码
        WEB_URL_SCAN_LOGIN = "http://debugweb.officego.com.cn/";
        SA_SERVER_URL = "https://officego.datasink.sensorsdata.cn/sa?project=default&token=d0db7a742f154aac";
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
        //员工管理
        H5_STAFF_LIST = APP_URL_MAIN + "owner/staffList.html";
        //融云IM test
        RC_APPKEY = "kj7swf8oknm02";
        RC_APPSECRET = "OF78PpILjjRk4";
        //bugly
        BUGLY_ID = "dc2ca7a8a6";
    }

    //生产
    @Override
    protected void initRelease(Context context, String env) {
        //URL
        APP_URL = "https://api.officego.com/";
        APP_URL_MAIN = "https://m.officego.com/";
        WEB_URL_SCAN_LOGIN = "http://webapi.officego.com/";
        SA_SERVER_URL = "https://officego.datasink.sensorsdata.cn/sa?project=production&token=d0db7a742f154aac";
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
        //员工管理
        H5_STAFF_LIST = APP_URL_MAIN + "owner/staffList.html";
        //融云IM release
        RC_APPKEY = "qf3d5gbjq94mh";
        RC_APPSECRET = "xtDkNCjJse";
        //bugly
        BUGLY_ID = "208a77af82";
    }

}
