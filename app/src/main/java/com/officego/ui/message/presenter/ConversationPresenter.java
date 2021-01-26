package com.officego.ui.message.presenter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.message.BuildingInfo;
import com.officego.commonlib.common.message.EcPhoneStatusInfo;
import com.officego.commonlib.common.message.EcPhoneWarnInfo;
import com.officego.commonlib.common.message.EcWeChatStatusInfo;
import com.officego.commonlib.common.message.PhoneInfo;
import com.officego.commonlib.common.message.ViewingDateInfo;
import com.officego.commonlib.common.message.ViewingDateStatusInfo;
import com.officego.commonlib.common.message.WeChatInfo;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.ExchangeContactsBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.RongUserInfoBean;
import com.officego.commonlib.common.rongcloud.SendMessageManager;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.db.LitepalUtils;
import com.officego.ui.message.HouseTags;
import com.officego.ui.message.contract.ConversationContract;

import java.util.HashMap;
import java.util.Map;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class ConversationPresenter extends BasePresenter<ConversationContract.View>
        implements ConversationContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private final boolean isMeetingEnter;

    public ConversationPresenter(boolean isMeetingEnter) {
        super();
        this.isMeetingEnter = isMeetingEnter;
    }

    /**
     * 获取聊天房源信息
     */
    @Override
    public void firstChatApp(String targetId, int buildingId, int houseId, String getHouseChatId) {
        if (!TextUtils.isEmpty(getHouseChatId)) {
            mView.showLoadingDialog();
            OfficegoApi.getInstance().getChatHouseDetails(buildingId, houseId, getHouseChatId,
                    new RetrofitCallback<ChatHouseBean>() {
                        @Override
                        public void onSuccess(int code, String msg, ChatHouseBean data) {
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                                mView.houseSuccess(data);
                                if (!isMeetingEnter && data != null)
                                    insertMessageInfo(targetId, data);
                            }
                        }

                        @Override
                        public void onFail(int code, String msg, ChatHouseBean data) {
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                            }
                        }
                    });
        }
    }

    /**
     * 判断是否可以交换手机和微信
     */
    @Override
    public void exchangeContactsVerification(String targetId) {
        if (!TextUtils.isEmpty(targetId)) {
            mView.showLoadingDialog();
            OfficegoApi.getInstance().exchangeContactsVerification(targetId,
                    new RetrofitCallback<ExchangeContactsBean>() {
                        @Override
                        public void onSuccess(int code, String msg, ExchangeContactsBean data) {
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                                if (data != null) {
                                    mView.exchangeContactsSuccess(data.isIsOk());
                                }
                            }
                        }

                        @Override
                        public void onFail(int code, String msg, ExchangeContactsBean data) {
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                                if (Constants.DEFAULT_ERROR_CODE == code) {
                                    mView.exchangeContactsFail(msg);
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 获取聊天房源信息
     */
    @Override
    public void isFirstChat(int buildingId, int houseId, String targetId) {
        OfficegoApi.getInstance().isChat(buildingId, houseId, targetId,
                new RetrofitCallback<FirstChatBean>() {
                    @Override
                    public void onSuccess(int code, String msg, FirstChatBean data) {
                        if (isViewAttached()) {
                            mView.firstChatSuccess(data);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, FirstChatBean data) {
                    }
                });
    }

    @Override
    public void getRongTargetInfo(String targetId) {
        if (!TextUtils.isEmpty(targetId)) {
            OfficegoApi.getInstance().getRongUserInfo(targetId,
                    new RetrofitCallback<RongUserInfoBean>() {
                        @Override
                        public void onSuccess(int code, String msg, RongUserInfoBean data) {
                            if (isViewAttached()) {
                                mView.rongTargetInfoSuccess(data);
                            }
                        }

                        @Override
                        public void onFail(int code, String msg, RongUserInfoBean data) {
                        }
                    });
        }
    }

    /**
     * 记录发送消息
     */
    @Override
    public void recordChatTime(String targetId, int houseId, int buildingId, Message message) {
        OfficegoApi.getInstance().recordChatTime(targetId, houseId, buildingId, chatMessage(message),
                new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                    }
                });
    }

    /**
     * 不同类型聊天消息内容
     *
     * @param message message
     * @return false
     */
    private String chatMessage(Message message) {
        String objectName = message.getObjectName();//消息类型
        String content;
        MessageContent messageContent = message.getContent();
        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            content = textMessage.getContent();
            LogCat.e(TAG, "onSent-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            content = imageMessage.getRemoteUri().toString();
            LogCat.e(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri().toString());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            content = voiceMessage.getUri().toString();
            LogCat.e(TAG, "onSent-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            content = richContentMessage.getContent();
            LogCat.e(TAG, "onSent-RichContentMessage:" + richContentMessage.getContent());
        } else if (messageContent instanceof EcPhoneStatusInfo) {//交换手机状态
            EcPhoneStatusInfo customMessage = (EcPhoneStatusInfo) messageContent;
            content = customMessage.getContent();
            LogCat.e(TAG, "onSent-EcPhoneStatusInfo:" + customMessage.getContent());
        } else if (messageContent instanceof EcPhoneWarnInfo) {//手机号码警告
            EcPhoneWarnInfo customMessage = (EcPhoneWarnInfo) messageContent;
            content = customMessage.getContent();
            LogCat.e(TAG, "onSent-EcPhoneWarnInfo:" + customMessage.getContent());
        } else if (messageContent instanceof EcWeChatStatusInfo) {//交换微信状态
            EcWeChatStatusInfo customMessage = (EcWeChatStatusInfo) messageContent;
            content = customMessage.getContent();
            LogCat.e(TAG, "onSent-EcWeChatStatusInfo:" + customMessage.getContent());
        } else if (messageContent instanceof PhoneInfo) {//交换手机
            PhoneInfo customMessage = (PhoneInfo) messageContent;
            content = customMessage.getContent();
            LogCat.e(TAG, "onSent-PhoneInfo:" + customMessage.getContent());
        } else if (messageContent instanceof ViewingDateInfo) {//预约看房
            ViewingDateInfo customMessage = (ViewingDateInfo) messageContent;
            content = customMessage.getContent();
            LogCat.e(TAG, "onSent-ViewingDateInfo:" + customMessage.getContent());
        } else if (messageContent instanceof ViewingDateStatusInfo) {//预约状态
            ViewingDateStatusInfo customMessage = (ViewingDateStatusInfo) messageContent;
            content = customMessage.getContent();
            LogCat.e(TAG, "onSent-ViewingDateStatusInfo: " + customMessage.getContent());
        } else if (messageContent instanceof WeChatInfo) {//交换微信
            WeChatInfo customMessage = (WeChatInfo) messageContent;
            content = customMessage.getContent();
            LogCat.e(TAG, "onSent-WeChatInfo: " + customMessage.getContent());
        } else {
            content = "";
            LogCat.e(TAG, "onSent-其他消息，自己来判断处理");
        }
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("objectName", objectName);
        return JSON.toJSONString(map);
    }

    //聊天是否插入消息
    private void insertMessageInfo(String targetId, ChatHouseBean data) {
        if (data.getIsBuildOrHouse() == 1) {
            //楼盘网点插入
            String mValue = SpUtils.getUserId() + data.getBuilding().getBuildingId() + targetId;
            if (LitepalUtils.getBuildingInfo(mValue) != null && TextUtils.equals(mValue, LitepalUtils.getBuildingInfo(mValue).getBuildingValue())) {
                return;
            }
            //插入消息
            SendMessageManager.getInstance().insertIncomingMessage(info(data)
                    , targetId, SpUtils.getRongChatId());
            //保存第一次插入状态
            String value = SpUtils.getUserId() + data.getBuilding().getBuildingId() + targetId;
            LitepalUtils.saveBuilding(value);
        } else {
            //房源插入
            String mValue = SpUtils.getUserId() + data.getHouse().getBuildingId() + data.getHouse().getHouseId() + targetId;
            if (LitepalUtils.getHouseInfo(mValue) != null && TextUtils.equals(mValue, LitepalUtils.getHouseInfo(mValue).getHouseValue())) {
                return;
            }
            //插入消息
            SendMessageManager.getInstance().insertIncomingMessage(info(data), targetId, SpUtils.getRongChatId());
            //保存第一次插入状态
            String value = SpUtils.getUserId() + data.getHouse().getBuildingId() + data.getHouse().getHouseId() + targetId;
            LitepalUtils.saveHouse(value);
        }
    }

    //插入楼盘信息
    private BuildingInfo info(ChatHouseBean data) {
        BuildingInfo info = new BuildingInfo();
        info.setIsBuildOrHouse(data.getIsBuildOrHouse());
        String showUser = TextUtils.equals(SpUtils.getUserId(), data.getCreateUser()) || TextUtils.isEmpty(data.getCreateUser()) ? "你" : "对方";
        String mDate;
        if (TextUtils.isEmpty(data.getCreateTime().toString()) || TextUtils.equals("0", data.getCreateTime().toString())) {
            mDate = DateTimeUtils.getStringDateTimeByStringPattern(DateTimeUtils.DateTimePattern.LONG_DATETIME_2);
        } else {
            mDate = DateTimeUtils.secondToDate(Long.parseLong(CommonHelper.bigDecimal(data.getCreateTime(), true)), "yyyy-MM-dd HH:mm");
        }
        info.setCreateTime(mDate + " 由" + showUser + "发起沟通");
        //1 楼盘网点 2房源
        if (data.getIsBuildOrHouse() == 1) {
            info.setBtype(data.getBuilding().getBtype());
            info.setBuildingId(data.getBuilding().getBuildingId());
            info.setHouseId(0);
            info.setbuildingName(data.getBuilding().getBuildingName());
            info.setImgUrl(data.getBuilding().getMainPic());
            info.setDistrict(data.getBuilding().getDistrict());
            if (data.getBuilding().getStationline().size() > 0) {
                String workTime = data.getBuilding().getNearbySubwayTime().get(0);
                String stationLine = data.getBuilding().getStationline().get(0);
                String stationName = data.getBuilding().getStationNames().get(0);
                info.setRouteMap("步行" + workTime + "分钟到「" + stationLine + "号线 · " + stationName + "」");
            }
            if (data.getBuilding().getMinSinglePrice() != null) {
                if (data.getBuilding().getBtype() == Constants.TYPE_BUILDING) {
                    info.setMinSinglePrice("¥" + data.getBuilding().getMinSinglePrice() + "/㎡/天");
                } else {
                    info.setMinSinglePrice("¥" + data.getBuilding().getMinSinglePrice() + "/位/月");
                }
            }
            if (data.getBuilding().getBtype() == Constants.TYPE_BUILDING) {
                info.setMinSinglePrice("¥" + (data.getBuilding().getMinSinglePrice() == null ? "0.0" : data.getBuilding().getMinSinglePrice()) + "/㎡/天");
            } else {
                info.setMinSinglePrice("¥" + (data.getBuilding().getMinSinglePrice() == null ? "0.0" : data.getBuilding().getMinSinglePrice()) + "/位/月");
            }
            if (data.getBuilding().getTags() != null && data.getBuilding().getTags().size() > 0) {
                info.setTags(HouseTags.getTags(data));
            }
        } else { //房源
            if (data.getHouse() != null) {
                info.setBtype(data.getHouse().getBtype());
                info.setBuildingId(data.getHouse().getBuildingId());
                info.setHouseId(data.getHouse().getHouseId());
                info.setbuildingName(data.getHouse().getHouseName());
                info.setImgUrl(data.getHouse().getMainPic());
                info.setDistrict(data.getHouse().getDistrict());
                if (data.getHouse().getStationline().size() > 0) {
                    String workTime = data.getHouse().getNearbySubwayTime().get(0);
                    String stationLine = data.getHouse().getStationline().get(0);
                    String stationName = data.getHouse().getStationNames().get(0);
                    info.setRouteMap("步行" + workTime + "分钟到「" + stationLine + "号线 · " + stationName + "」");
                }
                if (data.getHouse().getBtype() == Constants.TYPE_BUILDING) {
                    info.setMinSinglePrice("¥" + (data.getHouse().getMinSinglePrice() == null ? "0.0" : data.getHouse().getMinSinglePrice()) + "/㎡/天");
                } else {
                    info.setMinSinglePrice("¥" + (data.getHouse().getMinSinglePrice() == null ? "0.0" : data.getHouse().getMinSinglePrice()) + "/位/月");
                }
                if (data.getHouse().getTags() != null && data.getHouse().getTags().size() > 0) {
                    info.setTags(HouseTags.getHouseTags(data));
                }
            }
        }
        return info;
    }
}
