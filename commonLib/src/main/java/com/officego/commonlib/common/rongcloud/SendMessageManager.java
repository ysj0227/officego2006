package com.officego.commonlib.common.rongcloud;

import android.text.TextUtils;

import com.officego.commonlib.common.message.BuildingInfo;
import com.officego.commonlib.common.message.EcPhoneStatusInfo;
import com.officego.commonlib.common.message.EcWeChatStatusInfo;
import com.officego.commonlib.common.message.IdentityApplyInfo;
import com.officego.commonlib.common.message.IdentityApplyStatusInfo;
import com.officego.commonlib.common.message.PhoneInfo;
import com.officego.commonlib.common.message.ViewingDateInfo;
import com.officego.commonlib.common.message.ViewingDateStatusInfo;
import com.officego.commonlib.common.message.WeChatInfo;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.log.LogCat;

import java.util.Date;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Created by YangShiJie
 * Data 2020/5/26.
 * Descriptions: 自定义消息管理
 **/
public class SendMessageManager {
    protected final String TAG = this.getClass().getSimpleName();
    private static volatile SendMessageManager instance;

    public static SendMessageManager getInstance() {
        if (instance == null) {
            synchronized (IMManager.class) {
                if (instance == null) {
                    instance = new SendMessageManager();
                }
            }
        }
        return instance;
    }

    /**
     * 发送自定义消息 PhoneInfo
     */
    public void sendPhoneMessage(ChatHouseBean mData, String targetId, String content, String number, String extraMessage) {
        PhoneInfo info = new PhoneInfo();
        info.setContent(content);
        info.setNumber(number);
        info.setExtraMessage(extraMessage);
        //targetId是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, info);
        RongIM.getInstance().sendMessage(message, content, null, new IRongCallback.ISendMessageCallback() {
            @Override //表示消息添加到本地数据库
            public void onAttached(Message message) {

            }

            @Override//消息发送成功
            public void onSuccess(Message message) {
                //神策 发送交换手机
                String createTime = DateTimeUtils.formatDate("yyyy-MM-dd HH:mm:ss", new Date());
                int houseId = (mData == null || mData.getBuilding().getHouseId() == null) ? 0 :
                        Integer.parseInt(CommonHelper.bigDecimal(mData.getBuilding().getHouseId(), true));
                SensorsTrack.clickPhoneExchangeButton(mData == null ? 0 : mData.getBuilding().getBuildingId(),
                        houseId, message.getUId(), createTime);
            }

            @Override //消息发送失败
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    /**
     * 发送自定义消息 WeChatInfo
     */
    public void sendWeChatMessage(ChatHouseBean mData, String targetId, String content, String number, String extraMessage) {
        WeChatInfo info = new WeChatInfo();
        info.setContent(content);
        info.setNumber(number);
        info.setExtraMessage(extraMessage);
        //targetId是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, info);
        RongIM.getInstance().sendMessage(message, content, null, new IRongCallback.ISendMessageCallback() {
            @Override //表示消息添加到本地数据库
            public void onAttached(Message message) {
            }

            @Override//消息发送成功
            public void onSuccess(Message message) {
                //神策
                String createTime = DateTimeUtils.formatDate("yyyy-MM-dd HH:mm:ss", new Date());
                int houseId = (mData == null || mData.getBuilding().getHouseId() == null) ? 0 :
                        Integer.parseInt(CommonHelper.bigDecimal(mData.getBuilding().getHouseId(), true));
                SensorsTrack.clickWechatExchangeButton(mData == null ? 0 : mData.getBuilding().getBuildingId(),
                        houseId, message.getUId(), createTime);
            }

            @Override //消息发送失败
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    /**
     * 发送自定义消息 EcPhoneStatusInfo
     */
    public void sendEcPhoneStatusMessage(boolean isAgree, String targetId, String content,
                                         String sendNumber, String receiveNumber, String extraMessage) {
        EcPhoneStatusInfo info = new EcPhoneStatusInfo();
        info.setContent(content);
        info.setSendNumber(sendNumber);
        info.setReceiveNumber(receiveNumber);
        info.setExtraMessage(extraMessage);
        info.setAgree(isAgree);
        //targetId是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, info);
        RongIM.getInstance().sendMessage(message, content, null, callback);
    }

    /**
     * 发送自定义消息 EcWeChatStatus
     */
    public void sendEcWeChatStatusMessage(boolean isAgree, String targetId, String content,
                                          String sendNumber, String receiveNumber, String extraMessage) {
        EcWeChatStatusInfo info = new EcWeChatStatusInfo();
        info.setContent(content);
        info.setSendNumber(sendNumber);
        info.setReceiveNumber(receiveNumber);
        info.setExtraMessage(extraMessage);
        info.setAgree(isAgree);
        //targetId是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, info);
        RongIM.getInstance().sendMessage(message, content, null, callback);
    }

    /**
     * 发送自定义消息 预约看房
     */
    public void sendViewingDateMessage(String targetId, String houseId, String time,
                                       String buildingName, String buildingAddress,
                                       String content, String extraMessage) {
        ViewingDateInfo info = new ViewingDateInfo();
        info.setId(houseId);
        info.setTime(time);
        info.setBuildingName(buildingName);
        info.setBuildingAddress(buildingAddress);
        info.setContent(content);
        info.setExtraMessage(extraMessage);
        //targetId是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, info);
        RongIM.getInstance().sendMessage(message, content, null, callback);
    }

    /**
     * 发送自定义消息 预约看房同意拒绝
     */
    public void sendViewingDateStatusMessage(boolean isAgree, String targetId, String content, String extraMessage) {
        ViewingDateStatusInfo info = new ViewingDateStatusInfo();
        info.setAgree(isAgree);
        info.setContent(content);
        info.setExtraMessage(extraMessage);
        //targetId是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, info);
        RongIM.getInstance().sendMessage(message, content, null, callback);
    }


    /**
     * 发送自定义消息 认证申请
     */
    public void sendIdApplyMessage(String targetId, int id, int licenceId, String content, String extraMessage) {
        IdentityApplyInfo info = new IdentityApplyInfo();
        info.setId(id);
        info.setLicenceId(licenceId);
        info.setContent(content);
        info.setExtraMessage(extraMessage);
        //targetId是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, info);
        RongIM.getInstance().sendMessage(message, content, null, callback);
    }

    /**
     * 发送自定义消息 认证申请 同意拒绝
     */
    public void sendIdApplyStatusMessage(boolean isAgree, String targetId, String content, String extraMessage) {
        IdentityApplyStatusInfo info = new IdentityApplyStatusInfo();
        info.setAgree(isAgree);
        info.setContent(content);
        info.setExtraMessage(targetId);
        //targetId是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, info);
        RongIM.getInstance().sendMessage(message, content, null, callback);
    }

    /**
     * 插入自定义消息
     * setInsertIncomingMessage
     */
    public void insertIncomingMessage(BuildingInfo info, String targetId, String myId) {
        if (info != null) {
            RongIM.getInstance().insertIncomingMessage(
                    Conversation.ConversationType.PRIVATE,
                    targetId,
                    myId,
                    new Message.ReceivedStatus(1),
                    info,
                    resultCallback);
        }
    }

    /**
     * 自定义消息(插入)callback
     */
    private RongIMClient.ResultCallback<Message> resultCallback = new RongIMClient.ResultCallback<Message>() {

        @Override
        public void onSuccess(Message message) {
            LogCat.e("TAG", "set insert message success");
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {
            LogCat.e("TAG", "set insert message error");
        }
    };
    /**
     * 自定义消息callback
     */
    private IRongCallback.ISendMessageCallback callback = new IRongCallback.ISendMessageCallback() {
        @Override //表示消息添加到本地数据库
        public void onAttached(Message message) {

        }

        @Override//消息发送成功
        public void onSuccess(Message message) {
            LogCat.e("TAG", "message success");
        }

        @Override //消息发送失败
        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            LogCat.e("TAG", "message error");
        }
    };

    /**
     * 发送文本消息
     *
     * @param targetId targetId
     */
    public void sendTextMessage(String targetId, String content) {
        if (TextUtils.isEmpty(targetId)) {
            return;
        }
        TextMessage textMessage = TextMessage.obtain(content);
        RongIM.getInstance().sendMessage(Message.obtain(targetId, Conversation.ConversationType.PRIVATE,
                textMessage),
                content, null, new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                    }

                    @Override
                    public void onSuccess(Message message) {
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    }
                });
    }
}
