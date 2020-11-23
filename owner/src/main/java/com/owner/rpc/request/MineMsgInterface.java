package com.owner.rpc.request;

import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.VersionBean;
import com.officego.commonlib.retrofit.BaseResponse;
import com.owner.mine.model.AvatarBean;
import com.officego.commonlib.common.model.UserMessageBean;

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
public interface MineMsgInterface {

    String path = "api/";

    /**
     * 切换身份
     * 表单形式
     * 用户身份标：0租户，1户主
     */
    @Multipart
    @POST(path + "user/regTokenApp")
    Call<BaseResponse<LoginBean>> switchId(@PartMap Map<String, RequestBody> params);

    /**
     * 更新个人头像
     */
    @POST(path + "user/updateDataApp")
    Call<BaseResponse<AvatarBean>> updateUserAvatar(@Body RequestBody body);

    /**
     * 更新个人信息
     */
    @Multipart
    @POST(path + "user/updateDataApp")
    Call<BaseResponse<Object>> updateUserData(@PartMap Map<String, RequestBody> params);

//    /**
//     * 获取房东个人信息
//     */
//    @Multipart
//    @POST(path + "user/getUserInfoApp")
//    Call<BaseResponse<UserMessageBean>> getUserMsg(@PartMap Map<String, RequestBody> params);

    /**
     * 修改手机号
     */
    @Multipart
    @POST(path + "user/changePhone")
    Call<BaseResponse<Object>> modifyMobile(@PartMap Map<String, RequestBody> params);

    /**
     * 添加(绑定)微信
     */
    @Multipart
    @POST(path + "user/changeWxId")
    Call<BaseResponse<Object>> bindWechat(@PartMap Map<String, RequestBody> params);


    /**
     * 版本更新
     */
    @Multipart
    @POST(path + "version/android")
    Call<BaseResponse<VersionBean>> updateVersion(@PartMap Map<String, RequestBody> params);

}
