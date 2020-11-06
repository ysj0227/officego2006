package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.BuildingEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.JointWorkContract;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class JointWorkPresenter extends BasePresenter<JointWorkContract.View>
        implements JointWorkContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

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
    public void getBranchUnique() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getBranchUnique(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
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
    public void getRoomMatching() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().roomMatchingService(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.roomMatchingSuccess(data);
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
    public void getBaseService() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getBasicServices(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.baseServiceSuccess(data);
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
    public void getCompanyService() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getCompanyService(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.companyServiceSuccess(data);
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

    @Override
    public void saveEdit(int buildingId, int isTemp, int districtId, int area, String address,
                         String floorType, String totalFloor, String branchesTotalFloor,
                         String clearHeight, String airConditioning, String airConditioningFee,
                         String conferenceNumber, String conferencePeopleNumber, String roomMatching,
                         String parkingSpace, String ParkingSpaceRent,
                         String passengerLift, String cargoLift, String buildingIntroduction,
                         String internet, String settlementLicence, String tags,
                         String corporateServices, String basicServices,
                         String mainPic, String addImgUrl, String delImgUrl) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().jointWorkEditSave(buildingId, isTemp, districtId, area, address,
                floorType, totalFloor, branchesTotalFloor,
                clearHeight, airConditioning, airConditioningFee,
                conferenceNumber, conferencePeopleNumber, roomMatching,
                parkingSpace, ParkingSpaceRent,
                passengerLift, cargoLift, buildingIntroduction,
                internet, settlementLicence, tags,
                corporateServices, basicServices, mainPic, addImgUrl, delImgUrl, new RetrofitCallback<Object>() {
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