package com.officego.ui.home.presenter;

import android.content.Context;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.config.Constants;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.contract.BuildingDetailsJointWorkContract;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.ui.home.model.BuildingJointWorkBean;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class BuildingDetailsJointWorkPresenter extends BasePresenter<BuildingDetailsJointWorkContract.View>
        implements BuildingDetailsJointWorkContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;

    public BuildingDetailsJointWorkPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getBuildingDetails(String btype, String buildingId, String area, String dayPrice,
                                   String decoration, String houseTags, String seats) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getBuildingJointWorkDetails(btype, buildingId, area, dayPrice,
                decoration, houseTags, seats, new RetrofitCallback<BuildingJointWorkBean>() {
                    @Override
                    public void onSuccess(int code, String msg, BuildingJointWorkBean data) {
                        LogCat.e(TAG, "getBuildingDetails onSuccess =" + data);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.BuildingJointWorkDetailsSuccess(data);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, BuildingJointWorkBean data) {
                        LogCat.e(TAG, "getBuildingDetails onFail code=" + code + "  msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
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
                    if (code == Constants.ERROR_CODE_5002) {
                        mView.favoriteFail();
                    }
                }
            }
        });
    }

    @Override
    public void getBuildingSelectList(int pageNo, String btype, String buildingId, String area, String dayPrice, String decoration, String houseTags, String seats) {
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

}
