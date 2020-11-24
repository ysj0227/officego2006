package com.officego.rpc.request;

import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.VersionBean;
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
public interface MineMsgInterface {

    String path = "api/";

    /**
     * 切换身份
     * 表单形式
     *用户身份标：0租户，1户主
     * @return
     */
    @Multipart
    @POST(path + "user/regTokenApp")
    Call<BaseResponse<LoginBean>> switchId(@PartMap Map<String, RequestBody> params);

    /**
     * 添加微信
     *
     * @param params
     * @return
     */
    @Multipart
    @POST(path + "user/changeWxId")
    Call<BaseResponse<Object>> changeWechat(@PartMap Map<String, RequestBody> params);


    /**
     * 版本更新
     *
     * @param params
     * @return
     */
    @Multipart
    @POST(path + "version/android")
    Call<BaseResponse<VersionBean>> updateVersion(@PartMap Map<String, RequestBody> params);

}
