package com.officego.commonlib.common.presenter;

import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.contract.ConversationContract;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.ExchangeContactsBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.IdentitychattedMsgBean;
import com.officego.commonlib.common.model.RongUserInfoBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;

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
    public void firstChatApp(int buildingId, int houseId, String targetId) {
        if (!TextUtils.isEmpty(targetId)) {
            mView.showLoadingDialog();
            OfficegoApi.getInstance().getChatHouseDetails(buildingId, houseId, targetId,
                    new RetrofitCallback<ChatHouseBean>() {
                        @Override
                        public void onSuccess(int code, String msg, ChatHouseBean data) {
                            LogCat.e(TAG, "getDetails onSuccess =" + data);
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                                mView.houseSuccess(data);
                            }
                        }

                        @Override
                        public void onFail(int code, String msg, ChatHouseBean data) {
                            LogCat.e(TAG, "getDetails onFail code=" + code + "  msg=" + msg);
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
}
