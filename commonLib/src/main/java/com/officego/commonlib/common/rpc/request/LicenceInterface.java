package com.officego.commonlib.common.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface LicenceInterface {
    String path = "api/";

    /**
     * 认证公司，网点申请加入
     */
    @Multipart
    @POST(path + "licence/updateAuditStatusApp")
    Call<BaseResponse<Object>> updateAuditStatusApp(@PartMap Map<String, RequestBody> params);

}
