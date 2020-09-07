package com.officego.commonlib.common.presenter;

import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.contract.ConversationViewDateContract;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class ConversationViewDatePresenter extends BasePresenter<ConversationViewDateContract.View>
        implements ConversationViewDateContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    /**
     * 获取聊天房源信息
     */
    @Override
    public void getHouseDetails(int buildingId, int houseId, String targetId) {
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
}
