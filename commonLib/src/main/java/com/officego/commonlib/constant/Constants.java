package com.officego.commonlib.constant;

import com.officego.commonlib.base.BaseApplication;
import com.officego.commonlib.utils.CommonHelper;
import com.tencent.mm.opensdk.openapi.IWXAPI;

public class Constants {
    public static final String FILE_PROVIDER_AUTHORITY =
            CommonHelper.getAppPackageName(BaseApplication.getContext()) + ".Fileprovider";
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
    public static final int ERROR_CODE_5011 = 5011;//当前微信没有绑定手机号
    public static final int ERROR_CODE_666 = 666;//需要统一抛出提示的状态码
    public static final int ERROR_CODE_7012 = 7012;//楼盘已经下架
    public static final int ERROR_CODE_7013 = 7013;//楼盘下的房源已经售完
    public static final int ERROR_CODE_7014 = 7014;//楼盘已删除
    public static final int ERROR_CODE_7016 = 7016;//房源已经下架
    public static final int ERROR_CODE_6028 = 6028;//图片尺寸太大,宽高不能超过4096px
    //第三方app和微信通信的openapi接口
    public static IWXAPI WXapi;

    //微信
    public static final String APP_ID = "wx70ee9e90c1d62d83";//线上
    public static final String APP_ID_TEST = "wx0bc18fc3a299bd92";//测试
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

    //自定义类型业主楼盘管理
    public static final int BUILDING_FLAG_ADD = 1;
    public static final int BUILDING_FLAG_EDIT = 2;
    //楼盘，房源
    public static final int FLAG_BUILDING = 1000;
    public static final int FLAG_HOUSE = 1001;
    // 搜索筛选类型
    public static final int SEARCH_ALL = 0;//全部
    public static final int SEARCH_JOINT_WORK = 1;//共享
    public static final int SEARCH_OPEN_SEATS = 2;//开放工位
    public static final int SEARCH_OFFICE = 3;//办公室
    public static final int SEARCH_GARDEN = 4;//园区
    //h5 flags
    public final static int H5_HELP = 0;
    public final static int H5_PROTOCOL = 1;
    public final static int H5_PROTOCOL_SERVICE = 3;
    public final static int H5_ABOUTS = 2;
    public final static int H5_ROLE = 4;
    public final static int H5_VR_RECORD = 5;
    //客服
    public static final String SERVICE_HOT_MOBILE = "17321221162";
    public static final String SERVICE_TECHNICAL_SUPPORT = "13052007068";
    public static final String SERVICE_SUPPORT = "18016320482";
    //    租户端邮箱:service@officego.com
//    业主端邮箱:business@officego.com
    public static final String SERVICE_EMAIL_TENANT = "service@officego.com";
    public static final String SERVICE_EMAIL_OWNER = "business@officego.com";

    //楼盘，网点类型
    public final static int TYPE_BUILDING = 1;
    public final static int TYPE_JOINTWORK = 2;
    //分享
    public final static int TYPE_MEETING_ROOM = 5;
    //神策埋点筛选区域文本,装修类型文本
    public static String SENSORS_AREA_CONTENT = "";
    public static String SENSORS_DECORATION = "";
    //当前是否在消息的MessageFragment  切换账号为了刷新会话列表
    public static int TABLE_BAR_POSITION;
    //融云是否连接成功
    public static boolean isRCIMConnectSuccess;
    //经纬度高德地图
    public static String LONGITUDE = "";
    public static String LATITUDE = "";
    //上传图片类型 1楼图片2视频3房源图片 4认证图片
    public final static int TYPE_IMAGE_DEF = 0;
    public final static int TYPE_IMAGE_BUILDING = 1;
    public final static int TYPE_IMAGE_HOUSE = 3;
    public final static int TYPE_IMAGE_IDENTITY = 4;
    //总楼层 楼盘-网点
    public static String FLOOR_COUNTS = "";
    public static String FLOOR_JOINT_WORK_COUNTS = "";
    //业主--首页列表重新认证和新认证刷新，当有房源时切换tab不刷新
    public static boolean IS_HOME_REFRESH;
    //当前楼盘，网点名称
    public static String mCurrentBuildingName = "";
    //认证添加状态
    public final static int IDENTITY_FIRST = 1;
    public final static int IDENTITY_NO_FIRST = 2;
    public final static int IDENTITY_REJECT = 3;
}
