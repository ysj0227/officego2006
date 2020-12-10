package com.owner.mine.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.CouponDetailsBean;
import com.officego.commonlib.common.model.CouponWriteOffListBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.mine.contract.WriteOffContract;

/**
 * Created by shijie
 * Date 2020/12/8
 **/
public class WriteOffPresenter extends BasePresenter<WriteOffContract.View> implements
        WriteOffContract.Presenter {
    @Override
    public void getWriteOffList(int pageNo) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().couponSureWriteOffList(pageNo,new RetrofitCallback<CouponWriteOffListBean>() {
            @Override
            public void onSuccess(int code, String msg, CouponWriteOffListBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.writeOffListSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, CouponWriteOffListBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code== Constants.DEFAULT_ERROR_CODE){
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }
}
