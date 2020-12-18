package com.officego.commonlib.common.rpc.request;

import com.officego.commonlib.common.model.JPushLoginBean;
import com.officego.commonlib.retrofit.BaseResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by shijie
 * Date 2020/12/18
 **/
public interface JPushOneKeyLoginInterface {
    String path = "api/";

    /**
     * 一键登录 极光
     */
    @Multipart
    @POST(path + "login/jPushOneKeyLogin")
    Call<BaseResponse<JPushLoginBean>> getJPushPhone(@PartMap Map<String, RequestBody> params);
}
