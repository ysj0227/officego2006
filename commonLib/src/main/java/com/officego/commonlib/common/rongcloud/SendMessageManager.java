package com.officego.commonlib.common.rongcloud;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.common.message.BuildingInfo;
import com.officego.commonlib.common.message.EcPhoneStatusInfo;
import com.officego.commonlib.common.message.EcPhoneWarnInfo;
import com.officego.commonlib.common.message.EcWeChatStatusInfo;
import com.officego.commonlib.common.message.InsertLocalInfo;
import com.officego.commonlib.common.message.PhoneEncryptedInfo;
import com.officego.commonlib.common.message.PhoneInfo;
import com.officego.commonlib.common.message.TimeTipInfo;
import com.officego.commonlib.common.message.ViewingDateInfo;
import com.officego.commonlib.common.message.ViewingDateStatusInfo;
import com.officego.commonlib.common.message.WeChatInfo;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.log.LogCat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        RongIM.getInstance().sendMessage(message, content, content, new IRongCallback.ISendMessageCallback() {
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
        RongIM.getInstance().sendMessage(message, content, content, new IRongCallback.ISendMessageCallback() {
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
        RongIM.getInstance().sendMessage(message, content, content, callback);
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
        RongIM.getInstance().sendMessage(message, content, content, callback);
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
        RongIM.getInstance().sendMessage(message, content, content, callback);
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
        RongIM.getInstance().sendMessage(message, content, content, callback);
    }

    /**
     * 发送自定义消息 租户交换手机提示： 为避免电话被频繁骚扰，请谨慎交换电话
     */
    public void sendEcPhoneWarnsMessage(String targetId) {
        String content = "为避免电话被频繁骚扰，请谨慎交换电话";
        EcPhoneWarnInfo info = new EcPhoneWarnInfo();
        info.setContent(content);
        //targetId是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, info);
        RongIM.getInstance().sendMessage(message, content, content, callback);
    }

    /**
     * 插入手机号加密
     */
    public void insertPhoneEncryptedMessage(PhoneEncryptedInfo info, String targetId) {
        if (info != null) {
            RongIM.getInstance().insertIncomingMessage(
                    Conversation.ConversationType.PRIVATE,
                    targetId,
                    SpUtils.getRongChatId(),
                    new Message.ReceivedStatus(1), //1 send  2 receive
                    info,
                    resultCallback);
        }
    }

    /**
     * 弥补下拉刷新无法获取历史记录消息
     */
    public void localRefreshHistoryMessage(String targetId) {
        InsertLocalInfo info = new InsertLocalInfo();
        info.setContent("");
        RongIM.getInstance().insertIncomingMessage(
                Conversation.ConversationType.PRIVATE,
                targetId,
                SpUtils.getRongChatId(),
                new Message.ReceivedStatus(1), //1 send  2 receive
                info,
                resultCallback);
    }

    /**
     * 非工作时间段提示
     */
    @SuppressLint("SimpleDateFormat")
    public void insertTimeTipMessage(String targetId) {
        String currentData = DateTimeUtils.getCurrentDate();
        if (!TextUtils.equals(SpUtils.getCurrentDate(targetId), currentData)) {
            String format = "HH:mm:ss";
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                Date nowTime = sdf.parse(sdf.format(new Date()));
                Date startTime = sdf.parse("09:00:00");
                Date endTime = sdf.parse("18:00:00");
                if (!isEffectiveDate(nowTime, startTime, endTime)) {
                    SpUtils.saveCurrentDate(targetId);//当天日期
                    RongIM.getInstance().insertIncomingMessage(
                            Conversation.ConversationType.PRIVATE,
                            targetId,
                            SpUtils.getRongChatId(),
                            new Message.ReceivedStatus(1), //1 send  2 receive
                            TimeTipInfo.setData(""),
                            resultCallback);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        return date.after(begin) && date.before(end);
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
                    new Message.ReceivedStatus(1), //1 send  2 receive
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
