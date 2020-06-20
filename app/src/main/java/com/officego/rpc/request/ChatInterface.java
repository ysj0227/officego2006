package com.officego.rpc.request;

import com.officego.commonlib.retrofit.BaseResponse;
import com.officego.ui.home.model.ChatsBean;
import com.officego.commonlib.common.model.ChatHouseBean;

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
     * 楼盘房源详情进入聊天获取对方TargetId
     */
    @Multipart
    @POST(path + "chat/chatApp")
    Call<BaseResponse<ChatsBean>> getTargetId(@PartMap Map<String, RequestBody> params);

    /**
     * 聊天列表进入 获取房源详情
     */
    @Multipart
    @POST(path + "chat/firstChatApp")
    Call<BaseResponse<ChatHouseBean>> getChatHouseDetails(@PartMap Map<String, RequestBody> params);

}
