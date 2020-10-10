package com.officego.ui.home.presenter;

import android.content.Context;

import com.officego.R;
import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.retrofit.RpcErrorCode;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.contract.BuildingDetailsContract;
import com.officego.ui.home.model.BuildingDetailsBean;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.ui.home.model.ChatsBean;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class BuildingDetailsPresenter extends BasePresenter<BuildingDetailsContract.View>
        implements BuildingDetailsContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;


    public BuildingDetailsPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getBuildingDetails(String btype, String buildingId, String area, String dayPrice,
                                   String decoration, String houseTags, String seats) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getBuildingDetails(btype, buildingId, area, dayPrice,
                decoration, houseTags, seats, new RetrofitCallback<BuildingDetailsBean>() {
                    @Override
                    public void onSuccess(int code, String msg, BuildingDetailsBean data) {
                        LogCat.e(TAG, "getBuildingDetails onSuccess =" + data);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.BuildingDetailsSuccess(data);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, BuildingDetailsBean data) {
                        LogCat.e(TAG, "getBuildingDetails onFail code=" + code + "  msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code==Constants.ERROR_CODE_7012 || code==Constants.ERROR_CODE_7013 || code==Constants.ERROR_CODE_7014) {
                                mView.BuildingTakeOff(msg);
                            }
                        }
                    }
                });
    }

    @Override
    public void favorite(String buildingId, int flag) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().favorite(buildingId, flag, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                LogCat.e(TAG, "favorite onSuccess =" + data);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.favoriteSuccess();
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                LogCat.e(TAG, "favorite onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.ERROR_CODE_5002 || code == RpcErrorCode.RPC_ERR_TIMEOUT) {
                        mView.favoriteFail();
                    } else if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    @Override
    public void getBuildingSelectList(int pageNo, String btype, String buildingId, String area,
                                      String dayPrice, String decoration, String houseTags, String seats) {
        OfficegoApi.getInstance().getBuildingSelectList(pageNo, btype, buildingId, area, dayPrice,
                decoration, houseTags, seats, new RetrofitCallback<BuildingDetailsChildBean>() {
                    @Override
                    public void onSuccess(int code, String msg, BuildingDetailsChildBean data) {
                        LogCat.e(TAG, "getBuildingSelectList onSuccess =" + data);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.buildingSelectListSuccess(data.getTotal(), data.getList());
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, BuildingDetailsChildBean data) {
                        LogCat.e(TAG, "getBuildingSelectList onFail code=" + code + "  msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                        }
                    }
                });
    }

    /**
     * 聊天
     */
    @Override
    public void gotoChat(String buildingId) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getTargetId(buildingId, new RetrofitCallback<ChatsBean>() {
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
                    if (code==Constants.ERROR_CODE_5002||code==Constants.DEFAULT_ERROR_CODE){
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

}
