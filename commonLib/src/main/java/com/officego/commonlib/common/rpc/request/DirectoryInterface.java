package com.officego.commonlib.common.rpc.request;

import com.officego.commonlib.common.model.DirectoryBean;
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
 * Date 2020/10/15
 **/
public interface DirectoryInterface {
    String path = "api/";

    /**
     * @return 字典
     */
    @Multipart
    @POST(path + "dictionary/getDictionary")
    Call<BaseResponse<List<DirectoryBean.DataBean>>> getDictionary(@PartMap Map<String, RequestBody> params);
}
