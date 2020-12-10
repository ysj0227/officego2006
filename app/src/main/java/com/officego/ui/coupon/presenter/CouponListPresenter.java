package com.officego.ui.coupon.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.CouponListBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.ui.coupon.contract.CouponListContract;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class CouponListPresenter extends BasePresenter<CouponListContract.View>
        implements CouponListContract.Presenter {

    @Override
    public void getCouponList(int status) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getCouponList(status,
                new RetrofitCallback<CouponListBean>() {
                    @Override
                    public void onSuccess(int code, String msg, CouponListBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.endRefresh();
                            mView.couponListSuccess(data.getList());
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, CouponListBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                        }
                    }
                });
    }
}
