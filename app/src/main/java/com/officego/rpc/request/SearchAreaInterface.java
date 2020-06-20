package com.officego.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.officego.ui.find.model.DirectoryBean;
import com.officego.ui.home.model.BusinessCircleBean;
import com.officego.ui.home.model.MeterBean;

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
public interface SearchAreaInterface {

    String path = "api/";

    /**
     * 地铁
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "dictionary/getSubwayList")
    Call<BaseResponse<List<MeterBean.DataBean>>> getSubwayList(@PartMap Map<String, RequestBody> params);

    /**
     * @param
     * @return 商圈
     */
    @Multipart
    @POST(path + "dictionary/getDistrictList")
    Call<BaseResponse<List<BusinessCircleBean.DataBean>>> getDistrictList(@PartMap Map<String, RequestBody> params);


}
