package com.owner.identity2.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.IdentityRejectBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity2.contract.IdentityContract;
import com.owner.rpc.OfficegoApi;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/3
 **/
public class IdentityPresenter extends BasePresenter<IdentityContract.View>
        implements IdentityContract.Presenter {

    @Override
    public void searchBuilding(String keyword) {
        OfficegoApi.getInstance().searchListBuild(keyword, new RetrofitCallback<List<IdentityBuildingBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<IdentityBuildingBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.searchBuildingSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<IdentityBuildingBean.DataBean> data) {
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
    public void getIdentityMessage(int buildingId) {
        mView.showLoadingDialog();
        com.owner.rpc.OfficegoApi.getInstance().getIdentityMessage(buildingId, new RetrofitCallback<IdentityRejectBean>() {
            @Override
            public void onSuccess(int code, String msg, IdentityRejectBean data) {
                if (isViewAttached()) {
                    mView.identityMessageSuccess(data);
                    mView.hideLoadingDialog();
                }
            }

            @Override
            public void onFail(int code, String msg, IdentityRejectBean data) {
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
    public void submitIdentityMessage(int btype, int isFrist, String buildingName,
                                      String mainPic, String premisesPermit, String businessLicense,
                                      String materials, String idFront, String idBack, int isHolder,
                                      String buildId, int buildingId, int districtId, int businessDistrict,
                                      String address) {
        mView.showLoadingDialog();
        com.owner.rpc.OfficegoApi.getInstance().submitIdentity(btype, isFrist, buildingName,
                mainPic, premisesPermit, businessLicense,
                materials, idFront, idBack, isHolder,
                buildId, buildingId, districtId, businessDistrict,
                address, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.submitIdentitySuccess();
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
    public void uploadImage(int imageType, List<String> mFilePath) {
        //1楼图片2视频3房源图片4认证文件夹
        //认证上传图片
        mView.showLoadingDialog();
        com.owner.rpc.OfficegoApi.getInstance().uploadImage(Constants.TYPE_IMAGE_IDENTITY,
                mFilePath, new RetrofitCallback<UploadImageBean>() {
                    @Override
                    public void onSuccess(int code, String msg, UploadImageBean data) {
                        if (isViewAttached()) {
                            mView.uploadSuccess(imageType, data);
                            mView.hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, UploadImageBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE || code == Constants.ERROR_CODE_6028) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }
}
