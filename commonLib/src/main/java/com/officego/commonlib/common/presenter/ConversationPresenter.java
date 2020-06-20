package com.officego.commonlib.common.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.common.contract.ConversationContract;
import com.officego.commonlib.common.model.ChatHouseBean;

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
    public void getHouseDetails(String targetId) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getChatHouseDetails(targetId,
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
