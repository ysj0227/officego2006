package com.owner.identity2.presenter;

import com.officego.commonlib.base.BasePresenter;
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
    public void searchBuilding( String keyword) {
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
}
