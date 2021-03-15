package com.officego.wxapi;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.widget.Toast;

import com.officego.R;
import com.officego.commonlib.common.model.PayData;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_result);
        Constants.WXapi.handleIntent(getIntent(), this);
        Intent intent = getIntent();
        PayData payData = (PayData) intent.getSerializableExtra(Constants.WX_PAY);
        if (payData == null) {
            Toast.makeText(this, R.string.pay_error, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            pay(payData);
        }
    }

    /**
     * 开启支付
     */
    private void pay(PayData bean) {
        PayReq req = new PayReq();
        req.appId = Constants.APP_ID;
        req.partnerId = bean.getPartnerid();
        req.prepayId = bean.getPrepayid();
        req.nonceStr = bean.getNoncestr();
        req.timeStamp = bean.getTimestamp() + "";
        req.packageValue = bean.getPackageX();
        req.sign = bean.getSign();
        req.extData = "app_officego_pay"; // optional
        Constants.WXapi.sendReq(req);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Constants.WXapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * 0 则代表支付成功
     * -1为支付失败，包括用户主动取消支付，或者系统返回的错误
     * -2为取消支付，或者系统返回的错误
     * 其他为系统返回的错误
     */
    @SuppressLint("StringFormatInvalid")
    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = resp.errCode;
            String tip;
            if (errCode == 0) {
                CommonDialog dialog = new CommonDialog.Builder(this)
                        .setTitle("支付结果")
                        .setMessageDrawable(R.drawable.icon_image_select, 0, 0, 0)
                        .setMessageDrawablePadding(R.dimen.dp_5)
                        .setMessage("成功")
                        .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                            dialog12.dismiss();
                            finish();
                        }).create();
                dialog.showWithOutTouchable(false);
                dialog.setCancelable(false);
            } else if (errCode == -1) {
                tip = getString(R.string.pay_fail);
                result(tip);
            } else if (errCode == -2) {
                tip = getString(R.string.pay_cancel);
                result(tip);
            } else {
                tip = getString(R.string.pay_error);
                result(tip);
            }
        }
    }

    /**
     * 支付结果dialog
     */
    private void result(String tip) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}