package com.owner.mine.presenter;

import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.CouponDetailsBean;
import com.officego.commonlib.common.model.CouponWriteOffBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.mine.contract.CouponDetailsContract;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/10
 **/
public class CouponDetailsPresenter extends BasePresenter<CouponDetailsContract.View> implements
        CouponDetailsContract.Presenter {
    @Override
    public void getCouponDetails(String code) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getCouponDetails(code,new RetrofitCallback<CouponDetailsBean>() {
            @Override
            public void onSuccess(int code, String msg, CouponDetailsBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.couponDetailsSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, CouponDetailsBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code== Constants.DEFAULT_ERROR_CODE){
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    @Override
    public void sureWriteOff(List<CouponDetailsBean.BuildingMeetingroomListBean> arrayList,int couponId, String roomName) {
        mView.showLoadingDialog();
        int roomId = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (TextUtils.equals(arrayList.get(i).getTitle(), roomName)) {
                roomId = arrayList.get(i).getRoomId();
                break;
            }
        }
        OfficegoApi.getInstance().couponSureWriteOff(couponId,roomId,new RetrofitCallback<CouponWriteOffBean>() {
            @Override
            public void onSuccess(int code, String msg, CouponWriteOffBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.writeOffSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, CouponWriteOffBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code== Constants.DEFAULT_ERROR_CODE){
                        mView.shortTip(msg);
                    }
                    mView.writeOffFail();
                }
            }
        });
    }
}
