package com.officego.commonlib.common.presenter;

import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.contract.ConversationViewDateContract;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.owner.ChatBuildingBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;

import java.util.List;

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
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                                mView.houseSuccess(data);
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

    @Override
    public void getOwnerHouseList(String targetId) {
        if (!TextUtils.isEmpty(targetId)) {
            mView.showLoadingDialog();
            OfficegoApi.getInstance().chatBuildingList(Integer.parseInt(targetId),
                    new RetrofitCallback<List<ChatBuildingBean.DataBean>>() {
                        @Override
                        public void onSuccess(int code, String msg, List<ChatBuildingBean.DataBean> data) {
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                                mView.buildingListSuccess(data);
                            }
                        }

                        @Override
                        public void onFail(int code, String msg, List<ChatBuildingBean.DataBean> data) {
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                            }
                        }
                    });
        }
    }
}
