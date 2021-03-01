package com.officego.commonlib.common.rpc.request;

import com.officego.commonlib.common.model.ServiceBean;
import com.officego.commonlib.retrofit.BaseResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface ServiceInterface {
    String path = "api/";

    /**
     * @return service
     */
    @Multipart
    @POST(path + "main/getCustomerService")
    Call<BaseResponse<ServiceBean>> service(@PartMap Map<String, RequestBody> params);
}
