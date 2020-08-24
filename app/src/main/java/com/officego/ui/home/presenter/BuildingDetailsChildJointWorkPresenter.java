package com.officego.ui.home.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.contract.BuildingDetailsChildJointWorkContract;
import com.officego.ui.home.model.ChatsBean;
import com.officego.ui.home.model.HouseOfficeDetailsJointWorkBean;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class BuildingDetailsChildJointWorkPresenter extends BasePresenter<BuildingDetailsChildJointWorkContract.View>
        implements BuildingDetailsChildJointWorkContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;

    public BuildingDetailsChildJointWorkPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getDetails(String btype, String houseId) {
        if (!TextUtils.isEmpty(btype)) {
            mView.showLoadingDialog();
            OfficegoApi.getInstance().selectHousebyJointWorkHouseId(btype, houseId,
                    new RetrofitCallback<HouseOfficeDetailsJointWorkBean>() {
                        @Override
                        public void onSuccess(int code, String msg, HouseOfficeDetailsJointWorkBean data) {
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                                mView.detailsSuccess(data);
                            }
                        }

                        @Override
                        public void onFail(int code, String msg, HouseOfficeDetailsJointWorkBean data) {
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                            }
                        }
                    });
        }
    }

    @Override
    public void favoriteChild(String houseId, int flag) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().favoriteChild(houseId, flag, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.favoriteChildSuccess();
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    /**
     * 聊天
     */
    @Override
    public void gotoChat(String houseId) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getTargetId2(houseId, new RetrofitCallback<ChatsBean>() {
            @Override
            public void onSuccess(int code, String msg, ChatsBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.chatSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, ChatsBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.chatFail();
                    if (code == Constants.ERROR_CODE_5002 || code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }
}
