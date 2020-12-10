package com.officego.commonlib.common.rpc.request;

import com.officego.commonlib.common.model.CouponListBean;
import com.officego.commonlib.retrofit.BaseResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by shijie
 * Date 2020/12/10
 **/
public interface CouponInterface {

    String path = "api/";

    /**
     * @return 卡券列表
     */
    @Multipart
    @POST(path + "buildingMeetingroom/getCouponList")
    Call<BaseResponse<CouponListBean>> getCouponList(@PartMap Map<String, RequestBody> params);
}
