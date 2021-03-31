package com.owner.pay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.alipay.AlipayConfig;
import com.officego.commonlib.common.alipay.PayResult;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.Utils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.adapter.PayRightsAdapter;
import com.owner.h5.WebViewActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

/**
 * Created by shijie
 * Date 2021/3/15
 **/
@EActivity(resName = "activity_pay")
public class PayActivity extends BaseMvpActivity<PayPresenter>
        implements payContract.View {

    @ViewById(resName = "rl1")
    RelativeLayout rl1;
    @ViewById(resName = "tv_amount1")
    TextView tvAmount1;
    @ViewById(resName = "tv_one_month")
    TextView tvOneMonth;
    @ViewById(resName = "rl2")
    RelativeLayout rl2;
    @ViewById(resName = "tv_amount2")
    TextView tvAmount2;
    @ViewById(resName = "tv_three_month")
    TextView tvThreeMonth;
    @ViewById(resName = "tv_discount")
    TextView tvDiscount;
    @ViewById(resName = "rv_rights")
    RecyclerView rvRights;
    @ViewById(resName = "ibt_wx")
    RadioButton ibtWx;
    @ViewById(resName = "ibt_alipay")
    RadioButton ibtAlipay;
    @ViewById(resName = "tv_select_amount")
    TextView tvSelectAmount;
    @ViewById(resName = "tv_select_month")
    TextView tvSelectMonth;
    @Extra
    int buildingId;

    private int amounts = 1598;
    private final int amount1 = 698;
    private final int amount2 = 1598;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new PayPresenter(context);
        mPresenter.attachView(this);
        rvRights.setLayoutManager(new LinearLayoutManager(context));
        rvRights.setAdapter(new PayRightsAdapter(context, PayUtils.rightsList()));
    }

    @SuppressLint("SetTextI18n")
    @Click(resName = "rl1")
    void amount1Click() {
        rl1.setBackgroundResource(R.mipmap.ic_month_pay_selected);
        rl2.setBackgroundResource(R.mipmap.ic_month_pay_unselected);
        tvAmount1.setTextColor(ContextCompat.getColor(context, R.color.white));
        tvOneMonth.setTextColor(ContextCompat.getColor(context, R.color.white));
        tvAmount2.setTextColor(ContextCompat.getColor(context, R.color.common_red));
        tvThreeMonth.setTextColor(ContextCompat.getColor(context, R.color.text_33));
        tvDiscount.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
        tvSelectAmount.setText("¥" + amount1);
        tvSelectMonth.setText("(一个月)");
        amounts = amount1;
    }

    @SuppressLint("SetTextI18n")
    @Click(resName = "rl2")
    void amount2Click() {
        rl1.setBackgroundResource(R.mipmap.ic_month_pay_unselected);
        rl2.setBackgroundResource(R.mipmap.ic_month_pay_selected);
        tvAmount1.setTextColor(ContextCompat.getColor(context, R.color.common_red));
        tvOneMonth.setTextColor(ContextCompat.getColor(context, R.color.text_33));
        tvAmount2.setTextColor(ContextCompat.getColor(context, R.color.white));
        tvThreeMonth.setTextColor(ContextCompat.getColor(context, R.color.white));
        tvDiscount.setTextColor(ContextCompat.getColor(context, R.color.white));
        tvSelectAmount.setText("¥" + amount2);
        tvSelectMonth.setText("(三个月)");
        amounts = amount2;
    }

    @Click(resName = "ibt_wx")
    void payWXClick() {
        ibtWx.setChecked(true);
        ibtAlipay.setChecked(false);
    }

    @Click(resName = "ibt_alipay")
    void payAlipayClick() {
        ibtWx.setChecked(false);
        ibtAlipay.setChecked(true);
    }

    @Click(resName = "tv_pay_protocol")
    void payProtocolClick() {
        WebViewActivity_.intent(context).flags(Constants.H5_PAY).start();
    }

    @Click(resName = "ctl_pay")
    void payClick() {
        if (isFastClick(1200)) {
            return;
        }
        if (ibtWx.isChecked()) {
            mPresenter.weChatPay(String.valueOf(amounts), buildingId);
        } else {
            mPresenter.alipay(String.valueOf(amounts), buildingId);
        }
//        String env = Utils.getMetaValue(context, "ENV_DATA", AppConfig.ENV_TEST);
//        if (TextUtils.equals(env, AppConfig.ENV_RELEASE)) {
//            if (ibtWx.isChecked()) {
//                mPresenter.weChatPay(String.valueOf(amounts), buildingId);
//            } else {
//                mPresenter.alipay(String.valueOf(amounts), buildingId);
//            }
//        } else {
//            if (ibtWx.isChecked()) {
//                mPresenter.weChatPay("0.01", buildingId);
//            } else {
//                mPresenter.alipay("0.01", buildingId);
//            }
//        }
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
                        refreshBuildingList();
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
            refreshBuildingList();
        }
    }

    //popup支付成功
    private void refreshBuildingList() {
        BaseNotification.newInstance().postNotificationName(
                CommonNotifications.updateBuildingSuccess, "updateBuildingSuccess");
    }
}
