package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.BuildingEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.BuildingContract;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class BuildingPresenter extends BasePresenter<BuildingContract.View>
        implements BuildingContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void getBuildingUnique() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getBuildingUnique(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.houseUniqueSuccess(data);
                    mView.hideLoadingDialog();
                }
            }

            @Override
            public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                }
            }
        });
    }

    @Override
    public void getBuildingEdit(int buildingId, int isTemp) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getBuildingEdit(buildingId, isTemp, new RetrofitCallback<BuildingEditBean>() {
            @Override
            public void onSuccess(int code, String msg, BuildingEditBean data) {
                if (isViewAttached()) {
                    mView.buildingEditSuccess(data);
                    mView.hideLoadingDialog();
                }
            }

            @Override
            public void onFail(int code, String msg, BuildingEditBean data) {
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
    public void uploadImage(int type,List<ImageBean> mFilePath) {
        mView.showLoadingDialog();
        com.owner.rpc.OfficegoApi.getInstance().uploadImageUrl(type,mFilePath, new RetrofitCallback<UploadImageBean>() {
            @Override
            public void onSuccess(int code, String msg, UploadImageBean data) {
                if (isViewAttached()) {
                    mView.uploadSuccess(data);
                    mView.hideLoadingDialog();
                }
            }

            @Override
            public void onFail(int code, String msg, UploadImageBean data) {
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
     * 编辑保存
     */
    @Override
    public void saveEdit(int buildingId, int isTemp, int buildingType, String buildingNum,
                         int districtId, int area, String address, String totalFloor,
                         String completionTime, String refurbishedTime, String constructionArea,
                         String clearHeight, String storeyHeight, String property, String propertyCosts,
                         String parkingSpace, String ParkingSpaceRent, String airConditioning, String airConditioningFee,
                         String passengerLift, String cargoLift, String buildingIntroduction,
                         String internet, String settlementLicence,String tags,
                         String mainPic, String addImgUrl, String delImgUrl) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().buildingEditSave(buildingId, isTemp, buildingType, buildingNum,
                districtId, area, address, totalFloor,
                completionTime, refurbishedTime, constructionArea,
                clearHeight, storeyHeight, property, propertyCosts,
                parkingSpace, ParkingSpaceRent, airConditioning, airConditioningFee,
                passengerLift, cargoLift, buildingIntroduction,
                internet, settlementLicence, tags,mainPic, addImgUrl, delImgUrl, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.editSaveSuccess();
                            mView.hideLoadingDialog();
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

}