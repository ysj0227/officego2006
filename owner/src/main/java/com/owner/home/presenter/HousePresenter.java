package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.AddHouseSuccessBean;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.HouseContract;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class HousePresenter extends BasePresenter<HouseContract.View>
        implements HouseContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void getHouseUnique() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getHouseUnique(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
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
    public void getDecoratedType() {
//        mView.showLoadingDialog();
        OfficegoApi.getInstance().getDecoratedType(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
//                    mView.hideLoadingDialog();
                    mView.decoratedTypeSuccess(data);
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
    public void uploadImage(List<ImageBean> mFilePath) {
        mView.showLoadingDialog();
        com.owner.rpc.OfficegoApi.getInstance().uploadImageUrl(mFilePath, new RetrofitCallback<UploadImageBean>() {
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
    public void uploadSingleImage(String mFilePath) {
        mView.showLoadingDialog();
        com.owner.rpc.OfficegoApi.getInstance().uploadSingleImageUrl(mFilePath, new RetrofitCallback<UploadImageBean>() {
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
    public void saveEdit(int id, int isTemp, String title, String area,
                         String simple, String dayPrice, String monthPrice,
                         String floor, String clearHeight, String storeyHeight,
                         String minimumLease, String rentFreePeriod, String propertyHouseCosts,
                         String decoration, String unitPatternRemark, String tags,
                         String unitPatternImg, String mainPic, String addImgUrl, String delImgUrl) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().houseEditSave(id, isTemp, title, area,
                simple, dayPrice, monthPrice,
                floor, clearHeight, storeyHeight,
                minimumLease, rentFreePeriod, propertyHouseCosts,
                decoration, unitPatternRemark, tags,
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
    public void addHouse(int buildingId, int isTemp, String title, String area, String simple,
                         String dayPrice, String monthPrice, String floor, String clearHeight,
                         String storeyHeight, String minimumLease, String rentFreePeriod,
                         String propertyHouseCosts, String decoration, String unitPatternRemark,
                         String tags, String unitPatternImg, String mainPic,
                         String addImgUrl, String delImgUrl) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().addBuildingHouse(buildingId, isTemp, title, area,
                simple, dayPrice, monthPrice,
                floor, clearHeight, storeyHeight,
                minimumLease, rentFreePeriod, propertyHouseCosts,
                decoration, unitPatternRemark, tags,
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