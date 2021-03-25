package com.owner.pay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RadioButton;

import com.alipay.sdk.app.PayTask;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.alipay.AlipayConfig;
import com.officego.commonlib.common.alipay.PayResult;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
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
public class PayActivity extends BaseMvpActivity<PayPresenter>
        implements payContract.View {
    @ViewById(resName = "btn_wx")
    RadioButton btnWx;
    @ViewById(resName = "btn_alipay")
    RadioButton btnAlipay;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new PayPresenter(context);
        mPresenter.attachView(this);
    }

    @Click(resName = "btn_pay")
    void payClick() {
        if (btnWx.isChecked()) {
            mPresenter.weChatPay("1", 7762);
        } else if (btnAlipay.isChecked()) {
            mPresenter.alipay("0.01", 7762);
        } else {
            shortTip("请选择支付方式");
        }
    }

    /**
     * 支付宝支付start
     * ——————————————————————————————————
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
                        showAlert(PayActivity.this, getString(R.string.pay_result), getString(R.string.pay_success), true);
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        ToastUtils.showCenterToast(PayActivity.this, getString(R.string.pay_cancel));
                    } else {
                        showAlert(PayActivity.this, getString(R.string.pay_result), getString(R.string.pay_fail), false);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void alipayResult(String orderInfo) {
        final Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(PayActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            Message msg = new Message();
            msg.what = AlipayConfig.SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void showAlert(Context ctx, String title, String info, boolean isFinishActivity) {
        CommonDialog dialog = new CommonDialog.Builder(ctx)
                .setTitle(title)
                .setTitleSize(20F)
                .setMessage(info)
                .setConfirmButton(R.string.str_confirm, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    if (isFinishActivity) {
                        finish();
                    }
                })
                .setConfirmButtonSize(16F)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.weChatPaySuccess};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (id == CommonNotifications.weChatPaySuccess) {
            finish();
        }
    }
}
