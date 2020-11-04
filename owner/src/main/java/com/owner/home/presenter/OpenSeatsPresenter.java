package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.OpenSeatsContract;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/3
 **/
public class OpenSeatsPresenter extends BasePresenter<OpenSeatsContract.View>
        implements OpenSeatsContract.Presenter {
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

    @Override
    public void saveEdit(int id, int isTemp, String seats, String dayPrice,
                         String floor, String minimumLease, String rentFreePeriod,
                         String clearHeight, String mainPic,
                         String addImgUrl, String delImgUrl) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().openSeatsEditSave(id, isTemp, seats,
                dayPrice, floor, minimumLease, rentFreePeriod, clearHeight,
                mainPic, addImgUrl, delImgUrl, new RetrofitCallback<Object>() {
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
