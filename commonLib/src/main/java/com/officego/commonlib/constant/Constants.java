package com.officego.commonlib.constant;

import com.officego.commonlib.base.BaseApplication;
import com.officego.commonlib.utils.CommonHelper;

public class Constants {
    public static final String FILE_PROVIDER_AUTHORITY =
            CommonHelper.getAppPackageName(BaseApplication.getContext()) + ".fileprovider";
    //code
    public static final int DEFAULT_ERROR_CODE = 5000;//默认错误状态码,此状态必须写明 message 原因
    public static final int DEFAULT_SUCCESS_CODE = 200;//程序默认的成功状态码
    public static final int ERROR_CODE_5001 = 5001;//缺少参数或参数为空
    public static final int ERROR_CODE_5002 = 5002;//网络异常
    public static final int ERROR_CODE_5003 = 5003;//签名验证失败,请重新登录
    public static final int ERROR_CODE_5004 = 5004;//程序发生异常,请查看log日志
    public static final int ERROR_CODE_5005 = 5005;//参数类型错误
    public static final int ERROR_CODE_5006 = 5006;//暂无数据
    public static final int ERROR_CODE_5007 = 5007;//操作失败
    public static final int ERROR_CODE_5008 = 5008;//操作成功返回的data为null
    public static final int ERROR_CODE_5009 = 5009;//当前身份发生变化,请重新登录
    public static final int ERROR_CODE_666 = 666;//需要统一抛出提示的状态码

    //微信
//    public static final String APP_ID = "wx1c9f0ee3abf3b5e8";//本人
    public static final String APP_ID = "wx70ee9e90c1d62d83";//线上
    public static final String APP_SECRET = "b6e61dec5b24b9d5b0defc7865430cc4";
    //微信传值
    public static final String WX_TYPE = "WX_TYPE";
    public static final String WX_DATA = "WX_DATA";

    //倒计时
    public final static int SMS_TIME = 6_1000;
    //融云IM
    public final static String RC_APPKEY = "kj7swf8oknm02";
    public final static String RC_APPSECRET = "OF78PpILjjRk4";
    //租户
    public final static String TYPE_TENANT = "0";
    // 业主
    public final static String TYPE_OWNER = "1";
    //broadcast action
    public static final String BROADCAST_ACTION = "com.officego";
    public static final String BROADCAST_STATUS = "1000";

    //h5 flags
    public final static int H5_HELP = 0;
    public final static int H5_PROTOCOL = 1;
    public final static int H5_ABOUTS = 2;
    public final static int H5_SERVICE = 4;
    public final static int H5_OWNER_IDIFY = 5;
}
