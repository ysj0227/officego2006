package com.officego.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.officego.ui.find.model.DirectoryBean;
import com.officego.ui.home.model.QueryHistoryKeywordsBean;
import com.officego.ui.home.model.SearchListBean;

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
public interface SearchInterface {

    String path = "api/";

    /**
     * 全局搜索接口
     */
    @Multipart
    @POST(path + "esearch/searchListApp")
    Call<BaseResponse<List<SearchListBean.DataBean>>> searchList(@PartMap Map<String, RequestBody> params);

    /**
     * 添加搜索历史接口
     */
    @Multipart
    @POST(path + "esearch/addSearchKeywords")
    Call<BaseResponse<Object>> addSearchKeywords(@PartMap Map<String, RequestBody> params);

    /**
     * 清除搜索历史接口
     */
    @Multipart
    @POST(path + "esearch/delSearchKeywords")
    Call<BaseResponse<Object>> clearSearchKeywords(@PartMap Map<String, RequestBody> params);


    /**
     * 查询搜索历史接口
     */
    @Multipart
    @POST(path + "esearch/getSearchKeywords")
    Call<BaseResponse<List<QueryHistoryKeywordsBean.DataBean>>> getSearchKeywords(@PartMap Map<String, RequestBody> params);

}
