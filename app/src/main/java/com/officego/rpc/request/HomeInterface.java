package com.officego.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.officego.ui.home.model.BrandRecommendBean;
import com.officego.ui.home.model.BuildingBean;
import com.officego.ui.home.model.BuildingDetailsBean;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.ui.home.model.BuildingJointWorkBean;
import com.officego.ui.home.model.HomeHotBean;
import com.officego.ui.home.model.HomeMeetingBean;
import com.officego.ui.home.model.HouseOfficeDetailsBean;
import com.officego.ui.home.model.HouseOfficeDetailsJointWorkBean;
import com.officego.ui.home.model.TodayReadBean;

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


    /**
     * owner**********************************************
     * 业主端详情接口****************************************
     * ***************************************************
     */

    /**
     * 楼盘详情
     */
    @Multipart
    @POST(path + "building/getBuildingbyBuildingIdPreviewApp")
    Call<BaseResponse<BuildingDetailsBean>> getBuildingDetailsOwner(@PartMap Map<String, RequestBody> params);

    /**
     * 网点详情
     */
    @Multipart
    @POST(path + "building/getBuildingbyBuildingIdPreviewApp")
    Call<BaseResponse<BuildingJointWorkBean>> getBuildingJointWorkDetailsOwner(@PartMap Map<String, RequestBody> params);

    /**
     * 楼盘下--房源详情
     */
    @Multipart
    @POST(path + "house/getHousebyHouseIdPreviewApp")
    Call<BaseResponse<HouseOfficeDetailsBean>> selectHousebyHouseIdOwner(@PartMap Map<String, RequestBody> params);

    /**
     * 网点下--房源详情
     */
    @Multipart
    @POST(path + "house/getHousebyHouseIdPreviewApp")
    Call<BaseResponse<HouseOfficeDetailsJointWorkBean>> selectHousebyJointWorkHouseIdOwner(@PartMap Map<String, RequestBody> params);


    /**
     * 我想找
     */
    @Multipart
    @POST(path + "building/addWantGoBuildTemp")
    Call<BaseResponse<Object>> wantToFind(@PartMap Map<String, RequestBody> params);

    /**
     * 今日看点
     */
    @Multipart
    @POST(path + "building/getReadToday")
    Call<BaseResponse<List<TodayReadBean.DataBean>>> todayNews(@PartMap Map<String, RequestBody> params);

    /**
     * 品牌入驻
     */
    @Multipart
    @POST(path + "building/getBrandManagement")
    Call<BaseResponse<List<BrandRecommendBean.DataBean>>> getBrandManagement(@PartMap Map<String, RequestBody> params);

    /**
     * 会议室
     */
    @Multipart
    @POST(path + "building/getBuildingMeetingroom")
    Call<BaseResponse<HomeMeetingBean.DataBean>> getHomeMeeting(@PartMap Map<String, RequestBody> params);


    /**
     * 热门推荐
     */
    @Multipart
    @POST(path + "building/getHot")
    Call<BaseResponse<HomeHotBean.DataBean>> getHotList(@PartMap Map<String, RequestBody> params);

}
