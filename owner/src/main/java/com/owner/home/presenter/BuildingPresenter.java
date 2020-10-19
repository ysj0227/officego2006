package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.BuildingContract;

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

}