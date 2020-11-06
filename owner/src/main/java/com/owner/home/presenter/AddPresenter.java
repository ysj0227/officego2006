package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.AddContract;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.ImageBean;
import com.owner.rpc.OfficegoApi;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/3
 **/
public class AddPresenter extends BasePresenter<AddContract.View>
        implements AddContract.Presenter {

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
    public void searchBuilding(int flag,String keyword) {
        if (flag==0){
            OfficegoApi.getInstance().searchListBranch2(keyword, new RetrofitCallback<List<IdentityBuildingBean.DataBean>>() {
                @Override
                public void onSuccess(int code, String msg, List<IdentityBuildingBean.DataBean> data) {
                    if (isViewAttached()) {
                        mView.searchBuildingSuccess(data);
                    }
                }

                @Override
                public void onFail(int code, String msg, List<IdentityBuildingBean.DataBean> data) {

                }
            });
        }else {
            OfficegoApi.getInstance().searchListBuild(keyword, new RetrofitCallback<List<IdentityBuildingBean.DataBean>>() {
                @Override
                public void onSuccess(int code, String msg, List<IdentityBuildingBean.DataBean> data) {
                    if (isViewAttached()) {
                        mView.searchBuildingSuccess(data);
                    }
                }

                @Override
                public void onFail(int code, String msg, List<IdentityBuildingBean.DataBean> data) {

                }
            });
        }

    }

    @Override
    public void addBuilding(int btype, String buildingName, int districtId,
                            int businessDistrict, String address,
                            String mainPic, String premisesPermit, int buildingId) {
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().addBuilding(btype, buildingName, districtId,
                businessDistrict, address, mainPic, premisesPermit, buildingId, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.addSuccess();
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
