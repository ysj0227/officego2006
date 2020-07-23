package com.owner.identity.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.identity.contract.JointWorkContract;
import com.owner.identity.model.CheckIdentityBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.identity.model.IdentityJointWorkBean;
import com.owner.rpc.OfficegoApi;

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
    public void checkJointWork(int identityType, String name) {
        OfficegoApi.getInstance().checkBuildingByName(identityType, name, new RetrofitCallback<CheckIdentityBean>() {
            @Override
            public void onSuccess(int code, String msg, CheckIdentityBean data) {
                if (isViewAttached()) {
                    //0不存在1存在
                    if (data.getFlag() == 1) {
                        mView.shortTip(data.getExplain());
                    } else {
                        mView.checkJointWorkInfoSuccess();
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
    public void getJointWork(String keyword) {
        OfficegoApi.getInstance().searchListBranch(keyword, new RetrofitCallback<List<IdentityJointWorkBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<IdentityJointWorkBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.searchJointWorkSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<IdentityJointWorkBean.DataBean> data) {
                LogCat.e(TAG, "searchCompany onFail code=" + code + "  msg=" + msg);
            }
        });
    }

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
}