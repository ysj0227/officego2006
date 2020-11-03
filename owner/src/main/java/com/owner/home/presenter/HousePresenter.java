package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.HouseContract;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class HousePresenter extends BasePresenter<HouseContract.View>
        implements HouseContract.Presenter {
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
    public void getDecoratedType() {
//        mView.showLoadingDialog();
        OfficegoApi.getInstance().getDecoratedType(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
//                    mView.hideLoadingDialog();
                    mView.decoratedTypeSuccess(data);
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
}