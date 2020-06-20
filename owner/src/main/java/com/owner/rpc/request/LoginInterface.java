package com.owner.rpc.request;

import com.officego.commonlib.common.LoginBean;
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
     * 获取验证码
     * 表单形式
     * @return
     */
    @Multipart
    @POST(path + "login/sms_code")
    Call<BaseResponse<Object>> getSmsCode(@PartMap Map<String, RequestBody> params);

    /**
     * @param params
     * @return 登录
     */
    @Multipart
    @POST(path + "login/loginCode")
    Call<BaseResponse<LoginBean>> login(@PartMap Map<String, RequestBody> params);


}
