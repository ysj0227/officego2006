package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.IndependentContract;
import com.officego.commonlib.common.model.owner.AddHouseSuccessBean;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/3
 **/
public class IndependentPresenter extends BasePresenter<IndependentContract.View>
        implements IndependentContract.Presenter {
    @Override
    public void getHouseEdit(int houseId, int isTemp) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getHouseEdit(houseId, isTemp, new RetrofitCallback<HouseEditBean>() {
            @Override
            public void onSuccess(int code, String msg, HouseEditBean data) {
                if (isViewAttached()) {
                    mView.houseEditSuccess(data);
                    mView.hideLoadingDialog();
                }
            }

            @Override
            public void onFail(int code, String msg, HouseEditBean data) {
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
                    mView.uploadSuccess(false, data);
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

    @Override
    public void uploadSingleImage(int type,String mFilePath) {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().uploadSingleImageUrl(type,mFilePath, new RetrofitCallback<UploadImageBean>() {
            @Override
            public void onSuccess(int code, String msg, UploadImageBean data) {
                if (isViewAttached()) {
                    mView.uploadSuccess(true, data);
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

    @Override
    public void saveEdit(int id, int isTemp, String title, String seats,
                         String area, String monthPrice, String floor,
                         String minimumLease, String rentFreePeriod,
                         String conditioningType, String conditioningTypeCost,
                         String clearHeight, String unitPatternRemark,
                         String unitPatternImg, String mainPic, String addImgUrl, String delImgUrl) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().independentEditSave(id, isTemp, title, seats,
                area, monthPrice, floor, minimumLease, rentFreePeriod,
                conditioningType, conditioningTypeCost,
                clearHeight, unitPatternRemark,
                unitPatternImg, mainPic, addImgUrl, delImgUrl, new RetrofitCallback<Object>() {
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

    @Override
    public void addHouse(int buildingId,int isTemp, String title, String seats,
                         String area, String monthPrice, String floor,
                         String minimumLease, String rentFreePeriod,
                         String conditioningType, String conditioningTypeCost,
                         String clearHeight, String unitPatternRemark,
                         String unitPatternImg, String mainPic, String addImgUrl, String delImgUrl) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().addIndependentHouse(buildingId,isTemp, title, seats,
                area, monthPrice, floor, minimumLease, rentFreePeriod,
                conditioningType, conditioningTypeCost,
                clearHeight, unitPatternRemark,
                unitPatternImg, mainPic, addImgUrl, delImgUrl, new RetrofitCallback<AddHouseSuccessBean>() {
                    @Override
                    public void onSuccess(int code, String msg, AddHouseSuccessBean data) {
                        if (isViewAttached()) {
                            mView.addHouseSuccess(data.getId());
                            mView.hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, AddHouseSuccessBean data) {
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
