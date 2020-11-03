package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.IndependentContract;
import com.owner.home.contract.OpenSeatsContract;

/**
 * Created by shijie
 * Date 2020/11/3
 **/
public class OpenSeatsPresenter extends BasePresenter<OpenSeatsContract.View>
        implements OpenSeatsContract.Presenter {
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
