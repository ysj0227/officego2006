package com.officego.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.officego.commonlib.common.model.DirectoryBean;

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
public interface FindInterface {

    String path = "api/";

    /**
     * 获取房源特色
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "dictionary/getDictionary")
    Call<BaseResponse<List<DirectoryBean.DataBean>>> getHouseUnique(@PartMap Map<String, RequestBody> params);

    /**
     * @param
     * @return 类型
     */
    @Multipart
    @POST(path + "dictionary/getDictionary")
    Call<BaseResponse<List<DirectoryBean.DataBean>>> getDecoratedType(@PartMap Map<String, RequestBody> params);

   /**
     * @param
     * @return 搜索 热门
     */
    @Multipart
    @POST(path + "dictionary/getDictionary")
    Call<BaseResponse<List<DirectoryBean.DataBean>>> getHotKeywords(@PartMap Map<String, RequestBody> params);



    /**
     * @param
     * @return 我想找
     */
    @Multipart
    @POST(path + "building/addWantGoBuild")
    Call<BaseResponse<Object>> wantToFind(@PartMap Map<String, RequestBody> params);


}
