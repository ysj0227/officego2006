package com.officego.commonlib.common.rpc.request;

import com.officego.commonlib.common.model.BaseConfigbean;
import com.officego.commonlib.common.model.PayData;
import com.officego.commonlib.retrofit.BaseResponse;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by shijie
 * Date 2021/3/30
 **/
public interface MainInterface {

    String path = "api/";

    @Multipart
    @POST(path + "main/baseConf")
    Call<BaseResponse<BaseConfigbean>> baseConfig(@PartMap Map<String, RequestBody> params);
}
