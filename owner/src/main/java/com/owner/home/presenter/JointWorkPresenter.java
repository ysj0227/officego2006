package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.JointWorkContract;

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

}