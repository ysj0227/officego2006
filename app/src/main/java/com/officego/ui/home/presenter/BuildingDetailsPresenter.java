package com.officego.ui.home.presenter;

import android.content.Context;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.NearbyBuildingBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.retrofit.RpcErrorCode;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.contract.BuildingDetailsContract;
import com.officego.ui.home.model.BuildingDetailsBean;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.ui.home.model.ChatsBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class BuildingDetailsPresenter extends BasePresenter<BuildingDetailsContract.View>
        implements BuildingDetailsContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private final Context context;


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
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.BuildingDetailsSuccess(data);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, BuildingDetailsBean data) {
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
    public void getBuildingDetailsOwner(String btype, String buildingId, int isTemp) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getBuildingDetailsOwner(btype, buildingId, isTemp, new RetrofitCallback<BuildingDetailsBean>() {
                    @Override
                    public void onSuccess(int code, String msg, BuildingDetailsBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.BuildingDetailsSuccess(data);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, BuildingDetailsBean data) {
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
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.favoriteSuccess();
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
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
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.buildingSelectListSuccess(data.getTotal(), data.getList());
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, BuildingDetailsChildBean data) {
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

    @Override
    public void getNearbyBuildingList(int buildingId) {
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().nearbyBuildingList(
                buildingId, new RetrofitCallback<List<NearbyBuildingBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<NearbyBuildingBean.DataBean> data) {
                if (isViewAttached()) {
//                    LogCat.e(TAG,"11111="+data.getData().size());
                    mView.nearbyBuildingSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<NearbyBuildingBean.DataBean> data) {
            }
        });
    }

}
