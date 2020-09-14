package com.officego.ui.message.presenter;

import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.message.BuildingInfo;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.ExchangeContactsBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.IdentitychattedMsgBean;
import com.officego.commonlib.common.model.RongUserInfoBean;
import com.officego.commonlib.common.rongcloud.SendMessageManager;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.db.LitepalUtils;
import com.officego.ui.message.HouseTags;
import com.officego.ui.message.contract.ConversationContract;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class ConversationPresenter extends BasePresenter<ConversationContract.View>
        implements ConversationContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

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
                                if (data != null) {
                                    insertMessageInfo(targetId, data);
                                }
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
                        if (isViewAttached()) {
                        }
                    }
                });
    }

    @Override
    public void identityChattedMsg(String targetId) {
        OfficegoApi.getInstance().identityChattedMsg(targetId,
                new RetrofitCallback<IdentitychattedMsgBean>() {
                    @Override
                    public void onSuccess(int code, String msg, IdentitychattedMsgBean data) {
                        if (isViewAttached()) {
                            mView.identityChattedMsgSuccess(data);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, IdentitychattedMsgBean data) {
                        if (isViewAttached()) {
                        }
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
        String showUser = TextUtils.equals(SpUtils.getUserId(), data.getCreateUser()) ? "你" : "对方";
        String mDate;
        if (data.getCreateTime() == 0) {
            mDate = DateTimeUtils.getStringDateTimeByStringPattern(DateTimeUtils.DateTimePattern.LONG_DATETIME_2);
        } else {
            mDate = DateTimeUtils.secondToDate(data.getCreateTime(), "yyyy-MM-dd HH:mm");
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
                info.setRouteMap("步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName);
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
                    info.setRouteMap("步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName);
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
