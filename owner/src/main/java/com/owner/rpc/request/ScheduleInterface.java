package com.owner.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.owner.schedule.model.ViewingDateBean;

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
public interface ScheduleInterface {

    String path = "api/";

    /**
     * 看房列表
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "schedule/getScheduleListApp")
    Call<BaseResponse<List<ViewingDateBean.DataBean>>> getScheduleList(@PartMap Map<String, RequestBody> params);

    /**
     * 约看记录
     */
    @Multipart
    @POST(path + "schedule/getOldScheduleListApp")
    Call<BaseResponse<List<ViewingDateBean.DataBean>>> getOldScheduleList(@PartMap Map<String, RequestBody> params);

}
