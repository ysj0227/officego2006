package com.officego.commonlib.common.rpc.request;


import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.BuildingJointWorkBean;
import com.officego.commonlib.common.model.owner.HouseBean;
import com.officego.commonlib.retrofit.BaseResponse;

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
public interface BuildingJointWorkInterface {

    String path = "api/";

    /**
     * 楼盘或网点列表
     */
    @Multipart
    @POST(path + "building/selectBuildingList")
    Call<BaseResponse<BuildingJointWorkBean>> getBuildingJointWorkList(@PartMap Map<String, RequestBody> params);

    /**
     * 房源列表
     */
    @Multipart
    @POST(path + "house/selectHouseList")
    Call<BaseResponse<HouseBean>> getHouseList(@PartMap Map<String, RequestBody> params);

}
