package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.ServiceBean;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.common.model.owner.BuildingJointWorkBean;
import com.officego.commonlib.common.model.owner.HouseBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.home.contract.HomeContract;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void getBuildingJointWorkList() {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getBuildingJointWorkList(new RetrofitCallback<BuildingJointWorkBean>() {
            @Override
            public void onSuccess(int code, String msg, BuildingJointWorkBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.buildingJointWorkListSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, BuildingJointWorkBean data) {
                LogCat.e(TAG, "getUserInfo onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    @Override
    public void getHouseList(int buildingId, int isTemp, int pageNo) {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getHouseList(
                buildingId, isTemp, pageNo, new RetrofitCallback<HouseBean>() {
                    @Override
                    public void onSuccess(int code, String msg, HouseBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.houseListSuccess(data.getData(), data.getData().size() >= 10);
                            mView.endRefresh();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, HouseBean data) {
                        LogCat.e(TAG, "getUserInfo onFail code=" + code + "  msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.endRefresh();
                            if (code == Constants.DEFAULT_ERROR_CODE) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }

    @Override
    public void getUserInfo() {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getUserMsg(new RetrofitCallback<UserMessageBean>() {
            @Override
            public void onSuccess(int code, String msg, UserMessageBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.userInfoSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, UserMessageBean data) {
                LogCat.e(TAG, "getUserInfo onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    //用户账户是否过期
    @Override
    public void getUserExpireInfo() {
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getUserMsg(new RetrofitCallback<UserMessageBean>() {
            @Override
            public void onSuccess(int code, String msg, UserMessageBean data) {
                if (isViewAttached()) {
                    mView.userExpireSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, UserMessageBean data) {
            }
        });
    }

    //获取房源下的列表
    @Override
    public void getBuildingList() {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getBuildingJointWorkList(new RetrofitCallback<BuildingJointWorkBean>() {
            @Override
            public void onSuccess(int code, String msg, BuildingJointWorkBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.initHouseList(data);
                }
            }

            @Override
            public void onFail(int code, String msg, BuildingJointWorkBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    @Override
    public void isPublishHouse(int houseId, int isRelease, int isTemp) {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().isPublishHouse(
                houseId, isRelease, isTemp, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.publishOrOffHouseSuccess(isRelease);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        LogCat.e(TAG, "getUserInfo onFail code=" + code + "  msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }

    @Override
    public void houseDelete(int houseId, int isTemp) {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getHouseDelete(
                houseId, isTemp, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.houseDeleteSuccess();
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

    @Override
    public void getSupportMobile() {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().serviceMobile(new RetrofitCallback<ServiceBean>() {
            @Override
            public void onSuccess(int code, String msg, ServiceBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.supportMobileSuccess(data.getOwnerConsultation());
                }
            }

            @Override
            public void onFail(int code, String msg, ServiceBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.supportMobileSuccess(Constants.SERVICE_SUPPORT);
                }
            }
        });
    }
}