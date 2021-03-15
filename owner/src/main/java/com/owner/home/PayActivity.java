package com.owner.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioButton;

import com.alipay.sdk.app.PayTask;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.alipay.AlipayConfig;
import com.officego.commonlib.common.alipay.AuthResult;
import com.officego.commonlib.common.alipay.OrderInfoUtil2_0;
import com.officego.commonlib.common.alipay.PayResult;
import com.officego.commonlib.common.model.PayData;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.owner.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

/**
 * Created by shijie
 * Date 2021/3/15
 **/
@EActivity(resName = "activity_pay")
public class PayActivity extends BaseActivity {
    @ViewById(resName = "btn_wx")
    RadioButton btnWx;
    @ViewById(resName = "btn_alipay")
    RadioButton btnAlipay;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
    }


    @Click(resName = "btn_pay")
    void payClick() {
        if (btnWx.isChecked()) {
            gotoWxPayActivity();
        } else if (btnAlipay.isChecked()) {
            alipayV2();
        }else {
            shortTip("请选择支付方式");
        }
    }

    //跳转微信支付
    private void gotoWxPayActivity() {
        if (!CommonHelper.isInstallWechat(context)) {
            shortTip(R.string.str_need_install_wx);
            return;
        }
        //是否支持微信支付
        boolean isPaySupported = Constants.WXapi.getWXAppSupportAPI() >=
                com.tencent.mm.opensdk.constants.Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            shortTip(R.string.wx_str_no_support_pay);
            return;
        }

        OfficegoApi.getInstance().wxPay("1", new RetrofitCallback<PayData>() {
            @Override
            public void onSuccess(int code, String msg, PayData bean) {
                GotoActivityUtils.gotoWeChatPayActivity(context, bean);
            }

            @Override
            public void onFail(int code, String msg, PayData data) {

            }
        });
    }

    /**
     * 支付宝支付start
     * ——————————————————————————————————————————————————————————————————
     * ——————————————————————————————————————————————————————————————————
     */

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AlipayConfig.SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showAlert(PayActivity.this, getString(com.officego.commonlib.R.string.pay_success) + payResult);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showAlert(PayActivity.this, getString(com.officego.commonlib.R.string.pay_failed) + payResult);
                    }
                    break;
                }
                case AlipayConfig.SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        showAlert(PayActivity.this, getString(com.officego.commonlib.R.string.auth_success) + authResult);
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(PayActivity.this, getString(com.officego.commonlib.R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 支付宝支付业务示例
     */
    public void alipayV2() {
        if (TextUtils.isEmpty(AlipayConfig.APPID) || (TextUtils.isEmpty(AlipayConfig.RSA2_PRIVATE) && TextUtils.isEmpty(AlipayConfig.RSA_PRIVATE))) {
            showAlert(this, getString(com.officego.commonlib.R.string.error_missing_appid_rsa_private));
            return;
        }
        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         * orderInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (AlipayConfig.RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AlipayConfig.APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? AlipayConfig.RSA2_PRIVATE : AlipayConfig.RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = AlipayConfig.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private static void showAlert(Context ctx, String info) {
        showAlert(ctx, info, null);
    }

    private static void showAlert(Context ctx, String info, DialogInterface.OnDismissListener onDismiss) {
        new AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton(R.string.str_confirm, null)
                .setOnDismissListener(onDismiss)
                .show();
    }
    /**
     * 支付支付end
     * ——————————————————————————————————————————————————————————————————
     * ——————————————————————————————————————————————————————————————————
     */
}
