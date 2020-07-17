package com.owner.rpc.request;

import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.retrofit.BaseResponse;
import com.owner.identity.model.ApplyLicenceBean;
import com.owner.identity.model.BusinessCircleBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.identity.model.IdentityJointWorkBean;

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
     * @param
     * @return 商圈
     */
    @Multipart
    @POST(path + "dictionary/getDistrictList")
    Call<BaseResponse<List<BusinessCircleBean.DataBean>>> getDistrictList(@PartMap Map<String, RequestBody> params);

}
