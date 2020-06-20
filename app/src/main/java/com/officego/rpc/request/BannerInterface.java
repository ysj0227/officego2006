package com.officego.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.officego.ui.home.model.BannerBean;

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
public interface BannerInterface {

    String path = "api/";

    /**
     * 获取房源特色
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "banner/bannerList")
    Call<BaseResponse<List<BannerBean.DataBean>>> getBannerList(@PartMap Map<String, RequestBody> params);

}
