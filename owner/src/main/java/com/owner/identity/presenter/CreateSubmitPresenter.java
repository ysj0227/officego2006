package com.owner.identity.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.identity.contract.CreateSubmitContract;
import com.owner.identity.model.GetIdentityInfoBean;
import com.owner.rpc.OfficegoApi;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class CreateSubmitPresenter extends BasePresenter<CreateSubmitContract.View>
        implements CreateSubmitContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void getIdentityInfo(int identityType,boolean isFirstGetInfo) {
        OfficegoApi.getInstance().getIdentityInfo(identityType, new RetrofitCallback<GetIdentityInfoBean>() {
            @Override
            public void onSuccess(int code, String msg, GetIdentityInfoBean data) {
                if (isViewAttached()) {
                    mView.getIdentityInfoSuccess(data,isFirstGetInfo);
                }
            }

            @Override
            public void onFail(int code, String msg, GetIdentityInfoBean data) {
                if (isViewAttached()) {
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    @Override
    public void submitCompany(GetIdentityInfoBean data, int createCompany, int identityType,
                       String company, String address, String creditNo, String mStrPath) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().submitIdentityCreateCompany(data, createCompany, identityType,
                company, address, creditNo, mStrPath, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        LogCat.e(TAG, "1111 submitIdentityCreateCompany success");
                        if (isViewAttached()) {
                            mView.submitSuccess();
                            mView.hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        LogCat.e(TAG, "1111 submitIdentityCreateCompany onFail code=" + code + " msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE||code==Constants.ERROR_CODE_5002) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }

    @Override
    public void submitBuilding(GetIdentityInfoBean data, int createCompany, int identityType,
                               String buildingName, String address, int district, int business, String mPath) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().submitIdentityCreateBuilding(data, createCompany, identityType,
                buildingName,  address,  district,  business,mPath, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        LogCat.e(TAG, "1111 submitBuilding success");
                        if (isViewAttached()) {
                            mView.submitSuccess();
                            mView.hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        LogCat.e(TAG, "1111 submitBuilding onFail code=" + code + " msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE||code==Constants.ERROR_CODE_5002) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });

    }

    @Override
    public void submitJointWork(GetIdentityInfoBean data, int createCompany, int identityType,
                                String branchesName, String address, int district, int business, String mPath) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().submitIdentityCreateJointWork(data, createCompany, identityType,
                branchesName,  address,  district,  business,mPath, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        LogCat.e(TAG, "1111 submitIdentityCreateJointWork success");
                        if (isViewAttached()) {
                            mView.submitSuccess();
                            mView.hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        LogCat.e(TAG, "1111 submitIdentityCreateJointWork onFail code=" + code + " msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE||code==Constants.ERROR_CODE_5002) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }
}