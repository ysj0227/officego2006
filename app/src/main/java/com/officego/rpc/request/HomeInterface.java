package com.officego.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.officego.ui.home.model.BuildingBean;
import com.officego.ui.home.model.BuildingDetailsBean;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.ui.home.model.BuildingJointWorkBean;
import com.officego.ui.home.model.HouseOfficeDetailsBean;
import com.officego.ui.home.model.HouseOfficeDetailsJointWorkBean;

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
public interface HomeInterface {

    String path = "api/";

    /**
     * 首页列表数据
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "building/selectBuildingApp")
    Call<BaseResponse<BuildingBean>> getBuildingList(@PartMap Map<String, RequestBody> params);

    /**
     * 楼盘网点详情
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "building/selectBuildingbyBuildingIdApp")
    Call<BaseResponse<BuildingDetailsBean>> getBuildingDetails(@PartMap Map<String, RequestBody> params);

    /**
     * 网点详情
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "building/selectBuildingbyBuildingIdApp")
    Call<BaseResponse<BuildingJointWorkBean>> getBuildingJointWorkDetails(@PartMap Map<String, RequestBody> params);


    /**
     * 楼盘下房源列表
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "building/selectHouseApp")
    Call<BaseResponse<BuildingDetailsChildBean>> getBuildingSelectList(@PartMap Map<String, RequestBody> params);

    /**
     * 楼盘下房源详情
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "house/selectHousebyHouseIdApp")
    Call<BaseResponse<HouseOfficeDetailsBean>> selectHousebyHouseId(@PartMap Map<String, RequestBody> params);

    /**
     * 网点房源详情 共享办公
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "house/selectHousebyHouseIdApp")
    Call<BaseResponse<HouseOfficeDetailsJointWorkBean>> selectHousebyJointWorkHouseId(@PartMap Map<String, RequestBody> params);


}
