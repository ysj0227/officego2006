package com.owner.identity.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.retrofit.RpcErrorCode;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.identity.contract.PersonalContract;
import com.owner.identity.model.CheckIdentityBean;
import com.owner.identity.model.GetIdentityInfoBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.ImageBean;
import com.owner.rpc.OfficegoApi;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

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
    public void getIdentityInfo(int identityType, boolean isFirstGetInfo) {
        OfficegoApi.getInstance().getIdentityInfo(identityType, new RetrofitCallback<GetIdentityInfoBean>() {
            @Override
            public void onSuccess(int code, String msg, GetIdentityInfoBean data) {
                if (isViewAttached()) {
                    mView.getIdentityInfoSuccess(data, isFirstGetInfo);
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
                       boolean isSelectedBuilding, String buildingId, String buildingName, String buildingAddress,
                       String userName, String idCard,
                       String isCardFrontPath, String isCardBackPath,
                       List<ImageBean> mFilePremisesPath, List<ImageBean> mFileContractPath) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().submitPersonalIdentityInfo(data, createCompany, identityType, leaseType,
                isSelectedBuilding, buildingId, buildingName,buildingAddress,userName, idCard,
                isCardFrontPath, isCardBackPath,
                mFilePremisesPath, mFileContractPath, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.submitSuccess();
                            mView.hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        LogCat.e(TAG, "1111 submitPersonalIdentityInfo onFail code=" + code + " msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE || code == Constants.ERROR_CODE_5002) {
                                mView.shortTip(msg);
                            } else if (code == RpcErrorCode.RPC_COMMON_ERROR || code == 0) {
                                mView.submitTimeout();
                            }
                        }
                    }
                });
    }
    @Override
    public void deleteImage(boolean isPremisesImage,int id, int position) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().deleteImage(id, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.deleteImageSuccess(isPremisesImage,position);
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE || code == Constants.ERROR_CODE_5002) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }
}