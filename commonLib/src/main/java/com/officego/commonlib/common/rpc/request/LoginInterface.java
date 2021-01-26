package com.officego.commonlib.common.rpc.request;

import com.officego.commonlib.common.model.LoginBean;
import com.officego.commonlib.common.model.RCloudPushBean;
import com.officego.commonlib.common.model.WeChatAuthBean;
import com.officego.commonlib.retrofit.BaseResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by YangShiJie
 * Data 2020/5/8.
 * Descriptions:
 **/
public interface LoginInterface {

    String path = "api/";

    /**
     * 登录
     */
    @Multipart
    @POST(path + "login/loginCode")
    Call<BaseResponse<LoginBean>> login(@PartMap Map<String, RequestBody> params);

    /**
     * 融云推送
     */
    @Multipart
    @POST(path + "login/guest")
    Call<BaseResponse<RCloudPushBean>> getRCloudPush(@PartMap Map<String, RequestBody> params);

    /**
     * 微信授权获取信息
     */
    @Multipart
    @POST(path + "login/getWXAccessTokenForOpen")
    Call<BaseResponse<WeChatAuthBean>> getWeChatAuthInfo(@PartMap Map<String, RequestBody> params);

    /**
     * 微信授权登录校验是否绑定了手机号码
     */
    @Multipart
    @POST(path + "login/loginThird")
    Call<BaseResponse<LoginBean>> weChatLoginCheck(@PartMap Map<String, RequestBody> params);


}
