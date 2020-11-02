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

}