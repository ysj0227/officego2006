package com.officego.commonlib.common.rpc.request;


import com.officego.commonlib.common.model.IdentityRejectBean;
import com.officego.commonlib.common.model.owner.AddHouseSuccessBean;
import com.officego.commonlib.common.model.owner.BuildingEditBean;
import com.officego.commonlib.common.model.owner.BuildingJointWorkBean;
import com.officego.commonlib.common.model.owner.HouseBean;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.model.owner.RejectBuildingBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.retrofit.BaseResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
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

    /**
     * 上下架房源
     */
    @Multipart
    @POST(path + "house/houseReleaseOrShelves")
    Call<BaseResponse<Object>> getHouseRelease(@PartMap Map<String, RequestBody> params);

    /**
     * 房源删除
     */
    @Multipart
    @POST(path + "house/houseDelete")
    Call<BaseResponse<Object>> getHouseDelete(@PartMap Map<String, RequestBody> params);

    /**
     * 楼盘，网点编辑
     */
    @Multipart
    @POST(path + "building/getBuildingMsgByBuildingId")
    Call<BaseResponse<BuildingEditBean>> getBuildingEdit(@PartMap Map<String, RequestBody> params);

    /**
     * 房源编辑
     */
    @Multipart
    @POST(path + "house/getHouseMsgByHouseId")
    Call<BaseResponse<HouseEditBean>> getHouseEdit(@PartMap Map<String, RequestBody> params);

    /**
     * 上传图片
     */
    @POST(path + "upload/uploadResourcesUrl")
    Call<BaseResponse<UploadImageBean>> uploadResourcesUrl(@Body RequestBody body);

    /**
     * 楼盘编辑上传
     */
    @Multipart
    @POST(path + "building/updateBuilding")
    Call<BaseResponse<Object>> buildingEditSave(@PartMap Map<String, RequestBody> params);

    /**
     * 房源编辑上传
     */
    @Multipart
    @POST(path + "house/updateHouse")
    Call<BaseResponse<Object>> houseEditSave(@PartMap Map<String, RequestBody> params);

    /**
     * 房源添加
     */
    @Multipart
    @POST(path + "house/insertHouse")
    Call<BaseResponse<AddHouseSuccessBean>> houseAdd(@PartMap Map<String, RequestBody> params);


    /**
     * 楼盘发布上传VR
     */
    @Multipart
    @POST(path + "building/addBuildingVr")
    Call<BaseResponse<Object>> buildingPublishVr(@PartMap Map<String, RequestBody> params);

    /**
     * 楼盘发布上传VR
     */
    @Multipart
    @POST(path + "house/addHouseVr")
    Call<BaseResponse<Object>> housePublishVr(@PartMap Map<String, RequestBody> params);

    /**
     * 认证信息回显
     */
    @Multipart
    @POST(path + "building/getAttestation")
    Call<BaseResponse<IdentityRejectBean>> getAttestation(@PartMap Map<String, RequestBody> params);

    /**
     * 提交认证信息
     */
    @Multipart
    @POST(path + "building/addAttestationApp")
    Call<BaseResponse<Object>> addAttestationApp(@PartMap Map<String, RequestBody> params);

}
