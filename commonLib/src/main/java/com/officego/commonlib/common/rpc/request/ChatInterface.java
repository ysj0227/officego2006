package com.officego.commonlib.common.rpc.request;


import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.IdentitychattedMsgBean;
import com.officego.commonlib.common.model.RongUserInfoBean;
import com.officego.commonlib.common.model.ExchangeContactsBean;
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
public interface ChatInterface {

    String path = "api/";

    /**
     * 聊天列表进入 获取房源详情
     */
    @Multipart
    @POST(path + "chat/firstChatApp")
    Call<BaseResponse<ChatHouseBean>> getChatHouseDetails(@PartMap Map<String, RequestBody> params);

    /**
     * 聊天发送按钮APP 接口
     */
    @Multipart
    @POST(path + "chat/addChatApp")
    Call<BaseResponse<FirstChatBean>> isChat(@PartMap Map<String, RequestBody> params);

    /**
     * 判断是否可以交换手机和微信
     */
    @Multipart
    @POST(path + "chat/exchangePhoneVerification")
    Call<BaseResponse<ExchangeContactsBean>> exchangeContactsVerification(@PartMap Map<String, RequestBody> params);

    /**
     * 获取融云token
     */
    @Multipart
    @POST(path + "user/rongYunToken")
    Call<BaseResponse<Object>> getRongCloudToken(@PartMap Map<String, RequestBody> params);

    /**
     * 认证聊天获取信息
     */
    @Multipart
    @POST(path + "chat/chattedMsgApp")
    Call<BaseResponse<IdentitychattedMsgBean>> identityChattedMsg(@PartMap Map<String, RequestBody> params);

    /**
     * 认证聊天获取信息
     */
    @Multipart
    @POST(path + "user/getUser")
    Call<BaseResponse<RongUserInfoBean>> getRongUserInfo(@PartMap Map<String, RequestBody> params);

    /**
     * 聊天列表
     */
    @Multipart
    @POST(path + "chat/chatListApp")
    Call<BaseResponse<ChatListBean>> getChatList(@PartMap Map<String, RequestBody> params);

    /**
     * 聊天时间记录
     */
    @Multipart
    @POST(path + "chat/chatMsg")
    Call<BaseResponse<Object>> recordChatTime(@PartMap Map<String, RequestBody> params);

}
