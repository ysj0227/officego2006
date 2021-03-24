package com.owner.pay;

import android.content.Context;
import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.model.PayData;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.owner.R;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class PayPresenter extends BasePresenter<payContract.View>
        implements payContract.Presenter {
    private final Context context;

    public PayPresenter(Context context) {
        this.context = context;
    }

    /**
     * 跳转微信支付
     *
     * @param amount amount
     */
    @Override
    public void weChatPay(String amount, int buildingId) {
        //是否安装微信
        if (!CommonHelper.isInstallWechat(context)) {
            mView.shortTip(R.string.str_need_install_wx);
            return;
        }
        //是否支持微信支付
        boolean isPaySupported = Constants.WXapi.getWXAppSupportAPI() >=
                com.tencent.mm.opensdk.constants.Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            mView.shortTip(R.string.wx_str_no_support_pay);
            return;
        }
        mView.showLoadingDialog();
        OfficegoApi.getInstance().wxPay(amount, buildingId, new RetrofitCallback<PayData>() {
            @Override
            public void onSuccess(int code, String msg, PayData bean) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    GotoActivityUtils.gotoWeChatPayActivity(context, bean);
                }

            }

            @Override
            public void onFail(int code, String msg, PayData data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (Constants.DEFAULT_ERROR_CODE == code) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    /**
     * 跳转支付宝支付
     *
     * @param amount amount
     */
    @Override
    public void alipay(String amount, int buildingId) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().alipay(amount, buildingId, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object bean) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (Constants.DEFAULT_SUCCESS_CODE == code && !TextUtils.isEmpty(msg)) {
                        mView.alipayResult(msg);
                    } else {
                        mView.shortTip("订单生成失败");
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (Constants.DEFAULT_ERROR_CODE == code) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }
}