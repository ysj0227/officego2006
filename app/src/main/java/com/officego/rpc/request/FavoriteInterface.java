package com.officego.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.officego.ui.collect.model.CollectBuildingBean;
import com.officego.ui.collect.model.CollectHouseBean;

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
public interface FavoriteInterface {

    String path = "api/";

    /**
     * 收藏取消
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "favorite/addCollectionAPP")
    Call<BaseResponse<Object>> favorite(@PartMap Map<String, RequestBody> params);

    /**
     * 收藏楼盘列表
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "favorite/getFavoriteListApp")
    Call<BaseResponse<CollectBuildingBean>> favoriteBuildingList(@PartMap Map<String, RequestBody> params);

    //收藏房源列表
    @Multipart
    @POST(path + "favorite/getFavoriteListApp")
    Call<BaseResponse<CollectHouseBean>> favoriteHouseList(@PartMap Map<String, RequestBody> params);

}
