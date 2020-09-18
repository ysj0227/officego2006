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
    public static final String APP_ID = "wx70ee9e90c1d62d83";//线上
    public static final String APP_SECRET = "acfe3227435a5698455f81294a7b078e";
    //微信传值
    public static final String WX_TYPE = "WX_TYPE";
    public static final String WX_DATA = "WX_DATA";
    //倒计时
    public final static int SMS_TIME = 6_1000;

    //租户
    public final static String TYPE_TENANT = "0";
    // 房东
    public final static String TYPE_OWNER = "1";
    // 系统消息
    public final static String TYPE_SYSTEM = "3";

    //broadcast action
    public static final String BROADCAST_ACTION = "com.officego";
    public static final String BROADCAST_STATUS = "1000";

    //h5 flags
    public final static int H5_HELP = 0;
    public final static int H5_PROTOCOL = 1;
    public final static int H5_PROTOCOL_SERVICE = 3;
    public final static int H5_ABOUTS = 2;
    public final static int H5_ROLE = 4;

    //客服
    public static final String SERVICE_HOT_MOBILE = "17321221162";
    public static final String SERVICE_TECHNICAL_SUPPORT = "13052007068";
    //    租户端邮箱:service@officego.com
//    业主端邮箱:business@officego.com
    public static final String SERVICE_EMAIL_TENANT = "service@officego.com";
    public static final String SERVICE_EMAIL_OWNER = "business@officego.com";


    //楼盘，网点类型
    public final static int TYPE_BUILDING = 1;
    public final static int TYPE_JOINTWORK = 2;

    /**
     * 提交认证
     */
    //createCompany1提交认证2企业确认3楼盘、网点确认
    public final static int TYPE_CREATE_FROM_ALL = 1;
    public final static int TYPE_CREATE_FROM_COMPANY = 2;
    public final static int TYPE_CREATE_FROM_JOINT_BUILDING = 3;
    //identityType  身份类型0个人1企业2联合
    public final static int TYPE_IDENTITY_PERSONAL = 0;
    public final static int TYPE_IDENTITY_COMPANY = 1;
    public final static int TYPE_IDENTITY_JOINT_WORK = 2;

    //神策埋点筛选区域文本,装修类型文本
    public static String SENSORS_AREA_CONTENT = "";
    public static String SENSORS_DECORATION = "";

    //当前是否在消息的MessageFragment  切换账号为了刷新会话列表
    public static int TABLE_BAR_POSITION;
    //融云是否连接成功
    public static boolean isRCIMConnectSuccess;
}
