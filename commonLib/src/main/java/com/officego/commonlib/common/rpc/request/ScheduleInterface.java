package com.officego.commonlib.common.rpc.request;

import com.officego.commonlib.common.model.RenterBean;
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
public interface ScheduleInterface {

    String path = "api/";

//    /**
//     * @param
//     * @return 看房详情
//     */
//    @Multipart
//    @POST(path + "schedule/getScheduleApp")
//    Call<BaseResponse<ViewingDateDetailsBean>> getScheduleDetails(@PartMap Map<String, RequestBody> params);

    /**
     * @param
     * @return 预约看房
     */
    @Multipart
    @POST(path + "schedule/addRenterApp")
    Call<BaseResponse<RenterBean>> addRenter(@PartMap Map<String, RequestBody> params);

    /**
     * 行程审核
     * @param
     * @return
     */
    @Multipart
    @POST(path + "schedule/updateAuditStatus")
    Call<BaseResponse<Object>> updateAuditStatus(@PartMap Map<String, RequestBody> params);


}
