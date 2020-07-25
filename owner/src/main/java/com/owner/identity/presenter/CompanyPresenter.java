package com.owner.identity.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.identity.contract.CompanyContract;
import com.owner.identity.model.CheckIdentityBean;
import com.owner.identity.model.GetIdentityInfoBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.rpc.OfficegoApi;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class CompanyPresenter extends BasePresenter<CompanyContract.View>
        implements CompanyContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void getCompany(String keyword) {
        OfficegoApi.getInstance().searchCompany(keyword, new RetrofitCallback<List<IdentityCompanyBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<IdentityCompanyBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.searchCompanySuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<IdentityCompanyBean.DataBean> data) {
                LogCat.e(TAG, "searchCompany onFail code=" + code + "  msg=" + msg);
            }
        });
    }

    @Override
    public void getBuilding(String keyword) {
        OfficegoApi.getInstance().searchListBuild(keyword, new RetrofitCallback<List<IdentityBuildingBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<IdentityBuildingBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.searchBuildingSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<IdentityBuildingBean.DataBean> data) {
                LogCat.e(TAG, "searchListBuild onFail code=" + code + "  msg=" + msg);
            }
        });
    }

    @Override
    public void checkCompany(int identityType, String name) {
        OfficegoApi.getInstance().checkLicenceByCompany(identityType, name, new RetrofitCallback<CheckIdentityBean>() {
            @Override
            public void onSuccess(int code, String msg, CheckIdentityBean data) {
                if (isViewAttached()) {
                    //0不存在1存在
                    if (data.getFlag() == 1) {
                        mView.shortTip(data.getExplain());
                    } else {
                        mView.checkCompanyInfoSuccess();
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, CheckIdentityBean data) {
                if (isViewAttached()) {
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    @Override
    public void checkBuilding(int identityType, String name) {
        OfficegoApi.getInstance().checkBuildingByName(identityType, name, new RetrofitCallback<CheckIdentityBean>() {
            @Override
            public void onSuccess(int code, String msg, CheckIdentityBean data) {
                if (isViewAttached()) {
                    //0不存在1存在
                    if (data.getFlag() == 1) {
                        mView.shortTip(data.getExplain());
                    } else {
                        mView.checkBuildingInfoSuccess();
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, CheckIdentityBean data) {
                if (isViewAttached()) {
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    @Override
    public void getIdentityInfo(int identityType) {
        OfficegoApi.getInstance().getIdentityInfo(identityType, new RetrofitCallback<GetIdentityInfoBean>() {
            @Override
            public void onSuccess(int code, String msg, GetIdentityInfoBean data) {
                if (isViewAttached()) {
                    mView.getIdentityInfoSuccess(data);
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
    public void submit(GetIdentityInfoBean data, int createCompany, int identityType, int leaseType,
                       boolean isSelectedBuilding, String buildingId, List<String> mFilePremisesPath, List<String> mFileContractPath) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().submitCompanyIdentityInfo(data, createCompany, identityType, leaseType,
                isSelectedBuilding, buildingId, mFilePremisesPath, mFileContractPath, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.submitSuccess();
                            mView.hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        LogCat.e(TAG,"1111 submitCompanyIdentityInfo onFail code="+code+" msg="+msg);
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