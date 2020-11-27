package com.owner.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.owner.identity.model.BusinessCircleBean;

import java.util.List;
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
public interface IdentitySearchInterface {

    String path = "api/";

    /**
     * @param
     * @return 商圈
     */
    @Multipart
    @POST(path + "dictionary/getDistrictList")
    Call<BaseResponse<List<BusinessCircleBean.DataBean>>> getDistrictList(@PartMap Map<String, RequestBody> params);

}
