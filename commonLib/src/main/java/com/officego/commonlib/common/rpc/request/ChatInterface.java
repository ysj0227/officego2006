package com.officego.commonlib.common.rpc.request;


import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.IdentitychattedMsgBean;
import com.officego.commonlib.retrofit.BaseResponse;

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

}
