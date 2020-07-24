package com.owner.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.owner.identity.model.ApplyJoinBean;
import com.owner.identity.model.ApplyLicenceBean;
import com.owner.identity.model.BusinessCircleBean;
import com.owner.identity.model.CheckIdentityBean;
import com.owner.identity.model.GetIdentityInfoBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.identity.model.IdentityJointWorkBean;

import java.util.List;
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
public interface IdentitySearchInterface {

    String path = "api/";

    /**
     * 搜索企业
     */
    @Multipart
    @POST(path + "esearch/searchListByLicence")
    Call<BaseResponse<List<IdentityCompanyBean.DataBean>>> searchCompany(@PartMap Map<String, RequestBody> params);

    /**
     * 搜索楼盘
     */
    @Multipart
    @POST(path + "esearch/searchListBuild")
    Call<BaseResponse<List<IdentityBuildingBean.DataBean>>> searchListBuild(@PartMap Map<String, RequestBody> params);

    /**
     * 搜索网点
     */
    @Multipart
    @POST(path + "esearch/searchListBranch")
    Call<BaseResponse<List<IdentityJointWorkBean.DataBean>>> searchListBranch(@PartMap Map<String, RequestBody> params);


    /**
     * 认证公司，网点id信息
     */
    @Multipart
    @POST(path + "licence/selectApplyLicenceApp")
    Call<BaseResponse<ApplyLicenceBean>> selectApplyLicence(@PartMap Map<String, RequestBody> params);

    /**
     * 认证公司，网点申请加入
     */
    @Multipart
    @POST(path + "licence/applyLicenceProprietorApp")
    Call<BaseResponse<ApplyJoinBean>> applyLicenceProprietorApp(@PartMap Map<String, RequestBody> params);

    /**
     * 认证公司，网点撤销
     */
    @Multipart
    @POST(path + "licence/deleteUserLicenceApp")
    Call<BaseResponse<Object>> deleteUserLicenceApp(@PartMap Map<String, RequestBody> params);

    /**
     * @param
     * @return 商圈
     */
    @Multipart
    @POST(path + "dictionary/getDistrictList")
    Call<BaseResponse<List<BusinessCircleBean.DataBean>>> getDistrictList(@PartMap Map<String, RequestBody> params);

    /**
     * 企业名称创建校验接口
     */
    @Multipart
    @POST(path + "licence/selectLicenceByCompanyApp")
    Call<BaseResponse<CheckIdentityBean>> checkLicenceByCompany(@PartMap Map<String, RequestBody> params);

    /**
     * 网点创建校验接口
     */
    @Multipart
    @POST(path + "licence/selectBuildingByNameApp")
    Call<BaseResponse<CheckIdentityBean>> checkBuildingByName(@PartMap Map<String, RequestBody> params);

    /**
     * 提交认证   (创建公司， 网点 ，楼盘， 整体提交认证)
     */
    @POST(path + "licence/uploadLicenceProprietorApp")
    Call<BaseResponse<Object>> submitIdentityInfo(@Body RequestBody body);

    /**
     * 获取认证信息
     */
    @Multipart
    @POST(path + "licence/selectIdentityTypeApp ")
    Call<BaseResponse<GetIdentityInfoBean>> getLicenceProprietorInfo(@PartMap Map<String, RequestBody> params);

}
