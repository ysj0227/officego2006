package com.officego.commonlib.retrofit;

/**
 * Created by YangShiJie
 * Data 2020/5/8.
 * Descriptions:
 **/
public class RpcErrorCode {
    public static final int RPC_COMMON_ERROR = 990; //访问失败或无网络
    public static final int RPC_ERR_TIMEOUT = 991; //自定义的请求超时
    public static final int WHAT_ERROR = 997; //局域网访问设备失败或无网络

    public static final int HTTP_RESP_OK = 200; //请求成功
    public static final int ERROR_CODE_5001 = 5001;//缺少参数或参数为空

    //云端http请求 网关错误
    public static int HTTP_RESP_UNKNOWN_REQUEST = 400;//服务器不理解请求的语法
    public static int HTTP_RESP_TOKEN_ERR = 401;//缺少或者非法token
    public static int HTTP_RESP_FORBID = 403;//用户没有操作权限
    public static int HTTP_RESP_TOKEN_EXPIRE = 499;//token过期
}
