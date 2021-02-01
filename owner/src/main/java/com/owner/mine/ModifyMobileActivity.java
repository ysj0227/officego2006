package com.owner.mine;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.TextView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.RegexUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.owner.R;
import com.owner.mine.contract.ModifyMobileContract;
import com.owner.mine.presenter.ModifyMobilePresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

/**
 * Created by YangShiJie
 * Data 2020/6/18.
 * Descriptions:
 **/
@SuppressLint("Registered")
@EActivity(resName = "modify_activity_mobile")
public class ModifyMobileActivity extends BaseMvpActivity<ModifyMobilePresenter>
        implements ModifyMobileContract.View {

    @ViewById(resName = "et_mobile")
    ClearableEditText etMobile;
    @ViewById(resName = "et_sms_code")
    ClearableEditText etCode;
    @ViewById(resName = "tv_send_code")
    TextView tvGetCode;
    @ViewById(resName = "tv_mobile")
    TextView tvMobile;
    /**
     * 倒计时对象,总共的时间,每隔多少秒更新一次时间
     */
    final MyCountDownTimer mTimer = new MyCountDownTimer(Constants.SMS_TIME, 1000);
    private String mobile;

    @SuppressLint("SetTextI18n")
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        mPresenter = new ModifyMobilePresenter();
        mPresenter.attachView(this);
        String mobile = SpUtils.getPhoneNum();
        if (!TextUtils.isEmpty(mobile) && mobile.length() == 11) {
            String phoneNumber = mobile.substring(0, 3) + "****" + mobile.substring(7);
            tvMobile.setText("当前手机号：" + phoneNumber);
        }
        smsEditText();
    }

    //点击验证码输入框
    private void smsEditText() {
        etCode.setOnFocusChangeListener((view, b) -> {
            SensorsTrack.codeInput();//神策
        });
    }

    @Click(resName = "tv_send_code")
    void getCodeClick() {
        //神策
        SensorsTrack.smsCode();
        //发送验证码
        if (isMobileCorrect()) {
            return;
        }
        startDownTimer(mobile);
    }

    @Click(resName = "btn_modify")
    void modifyMobileClick() {
        if (isMobileCorrect()) {
            return;
        }
        String code = etCode.getText() == null ? "" : etCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            shortTip(R.string.str_please_input_sms_code);
            return;
        }
        mPresenter.modifyMobile(mobile, code);
    }

    private boolean isMobileCorrect() {
        if (isFastClick(1200)) {
            return true;
        }
        mobile = RegexUtils.handleIllegalCharacter(etMobile.getText() == null ? "" :
                etMobile.getText().toString().trim());
        if (TextUtils.isEmpty(mobile)) {
            shortTip("请输入手机号");
            return true;
        }
        if (!RegexUtils.isChinaPhone(mobile)) {
            shortTip(R.string.tip_input_correct_phone);
            return true;
        }
        if (TextUtils.equals(mobile, SpUtils.getPhoneNum())) {
            shortTip("此手机号已注册");
            return true;
        }
        return false;
    }

    private void startDownTimer(String mobile) {
        mPresenter.getSms(mobile);
        mTimer.start();
    }

    @UiThread
    void stopDownTimer() {
        mTimer.cancel();
        tvGetCode.setText(getResources().getString(R.string.str_resend));
        tvGetCode.setClickable(true);
    }

    @Override
    public void modifyMobileSuccess() {
        //重新登录
        GotoActivityUtils.loginClearActivity(context);
        finish();
    }

    //倒计时函数
    private class MyCountDownTimer extends CountDownTimer {
        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            tvGetCode.setClickable(false);
            tvGetCode.setTextColor(getResources().getColor(R.color.text_normal));
            tvGetCode.setText(String.format(Locale.getDefault(), "%ds", l / 1000));
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            stopDownTimer();
            tvGetCode.setTextColor(getResources().getColor(R.color.common_blue_main));
            tvGetCode.setText(getResources().getString(R.string.str_resend));
            tvGetCode.setClickable(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDownTimer();
    }

}
