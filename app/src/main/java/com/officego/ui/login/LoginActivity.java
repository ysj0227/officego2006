package com.officego.ui.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.officego.MainActivity_;
import com.officego.MainOwnerActivity_;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NotificationUtil;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.RegexUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.h5.WebViewActivity_;
import com.officego.ui.home.VideoPlayerActivity;
import com.officego.ui.login.contract.LoginContract;
import com.officego.ui.login.presenter.LoginPresenter;
import com.officego.utils.MonitorEditTextUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;
import java.util.Objects;

import io.rong.pushperm.ResultCallback;
import io.rong.pushperm.RongPushPremissionsCheckHelper;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/

@SuppressLint("Registered")
@EActivity(R.layout.login_activity)
public class LoginActivity extends BaseMvpActivity<LoginPresenter>
        implements LoginContract.View {
    @ViewById(R.id.btn_login)
    Button btnLogin;
    @ViewById(R.id.et_mobile)
    ClearableEditText etMobile;
    @ViewById(R.id.et_code)
    ClearableEditText etCode;
    @ViewById(R.id.tv_get_code)
    TextView tvGetCode;
    @ViewById(R.id.tv_protocol)
    TextView tvProtocol;
    @ViewById(R.id.rl_code)
    RelativeLayout rlCode;
    @ViewById(R.id.rl_back)
    RelativeLayout rlBack;
    @ViewById(R.id.btn_login_no_password)
    Button btnLoginNoPassword;
    @ViewById(R.id.btn_test)
    Button btnTest;
    //关闭当前租户页面
    @Extra
    boolean isFinishCurrentView;
    private String mobile;
    //房东model修改密码重新登录
    private boolean isOwnerLogin;
    //倒计时对象,总共的时间,每隔多少秒更新一次时间
    final MyCountDownTimer mTimer = new MyCountDownTimer(Constants.SMS_TIME, 1000);

    @AfterViews
    void init() {
        PermissionUtils.checkPermissionActivity(this);
        channelTrack();
        StatusBarUtils.setStatusBarColor(this);
        mPresenter = new LoginPresenter(context);
        mPresenter.attachView(this);
        new MonitorEditTextUtils(btnLogin, etMobile);
        tvProtocol.setText(Html.fromHtml(getString(R.string.str_click_login_agree_service)));
        if (getIntent().getExtras() != null) {
            isOwnerLogin = getIntent().getExtras().getBoolean("isOwnerLogin");
            rlBack.setVisibility(isOwnerLogin ? View.GONE : View.VISIBLE);
        }
        if (!TextUtils.isEmpty(SpUtils.getSignToken())) {
            if (TextUtils.equals(Constants.TYPE_OWNER, String.valueOf(SpUtils.getRole()))) {
                MainOwnerActivity_.intent(context).start();
            } else {
                MainActivity_.intent(context).start();
            }
        } else {
            NotificationUtil.showSettingDialog(context);
        }
        smsEditText();
    }

    //点击验证码输入框
    private void smsEditText() {
        etCode.setOnFocusChangeListener((view, b) -> {
            SensorsTrack.codeInput();//神策
        });
    }

    @Click(R.id.btn_login)
    void loginClick() {
        if (isFastClick(1500)) {
            return;
        }
        mobile = RegexUtils.handleIllegalCharacter(etMobile.getText().toString().trim());
        if (rlCode.isShown()) {
            //神策
            SensorsTrack.login();
            String code = Objects.requireNonNull(etCode.getText()).toString().trim();
            if (TextUtils.isEmpty(code)) {
                shortTip(R.string.str_please_input_sms_code);
                return;
            }
            //登录
            mPresenter.login(mobile, code);
        } else {
            //神策
            SensorsTrack.smsCode();
            if (!RegexUtils.isChinaPhone(mobile)) {
                shortTip(R.string.tip_input_correct_phone);
                return;
            }
            rlCode.setVisibility(View.VISIBLE);
            btnLogin.setText(R.string.str_login);
            //发送验证码
            startDownTimer(mobile);
        }
    }

    /**
     * 本机免密登录
     */
    @Click(R.id.btn_login_no_password)
    void loginNoPasswordClick() {
//        startActivity(new Intent(this, VideoPlayerActivity.class));
        if (isFastClick(1500)) {
            return;
        }
        //神策
        SensorsTrack.login();
        //手机权限
        if (PermissionUtils.checkPhonePermission(this)) {
            loginOnlyPhone();
        }
    }

    private void loginOnlyPhone() {
        if (!TextUtils.isEmpty(CommonHelper.getPhoneNum(context))) {
            mPresenter.loginOnlyPhone(CommonHelper.getPhoneNum(context));
        } else {
            shortTip(R.string.str_cannot_get_mine_phone_use_sms);
        }
    }

    @Click(R.id.btn_test)
    void testClick() {
        testDialog(context);
    }

    @Click(R.id.tv_get_code)
    void getCodeClick() {
        if (isFastClick(1200)) {
            return;
        }
        //神策
        SensorsTrack.smsCode();
        //发送验证码
        mobile = RegexUtils.handleIllegalCharacter(etMobile.getText().toString().trim());
        startDownTimer(mobile);
    }

    @Click(R.id.rl_back)
    void backClick() {
        if (isFastClick(1200)) {
            return;
        }
        finish();
    }

    @Click(R.id.tv_protocol)
    void protocolClick() {
        if (isFastClick(1200)) {
            return;
        }
        WebViewActivity_.intent(context).flags(Constants.H5_PROTOCOL).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SensorsTrack.trackInstallation(context);
            btnLoginNoPassword.setVisibility(TextUtils.isEmpty(CommonHelper.getPhoneNum(context)) ? View.GONE : View.VISIBLE);
        }
        if (grantResults.length <= 0) {
            return;
        }
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//用户拒绝
            shortTip(R.string.tip_permission_ungranted);
        }
    }

    @Override
    public void sendSmsSuccess() {
        shortTip(R.string.tip_sms_code_send_success);
    }

    @Override
    public void loginSuccess(LoginBean data) {
        //神策
        SensorsTrack.sensorsLogin(data.getUid());
        //登录成功跳转
        SpUtils.saveRole(String.valueOf(data.getRid()));
        if (!isFinishCurrentView) {
            if (TextUtils.equals(Constants.TYPE_OWNER, String.valueOf(data.getRid()))) {
                MainOwnerActivity_.intent(context).start();
            } else {
                MainActivity_.intent(context).start();
            }
        }
        finish();
    }

    @Override
    public void loginFail(int code, String msg) {
        shortTip(msg);
    }

    private void startDownTimer(String mobile) {
        mPresenter.sendSmsCode(mobile);
        mTimer.start();
    }

    @UiThread
    void stopDownTimer() {
        mTimer.cancel();
        tvGetCode.setText(getResources().getString(R.string.str_resend));
        tvGetCode.setClickable(true);
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

    //渠道追踪
    private void channelTrack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    "android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED) {
                SensorsTrack.trackInstallation(context);
                btnLoginNoPassword.setVisibility(TextUtils.isEmpty(CommonHelper.getPhoneNum(context)) ? View.GONE : View.VISIBLE);
            }
        } else {
            SensorsTrack.trackInstallation(context);
            btnLoginNoPassword.setVisibility(TextUtils.isEmpty(CommonHelper.getPhoneNum(context)) ? View.GONE : View.VISIBLE);
        }
    }

    //融云消息推送的手机权限设置
    private void rongPermissionSetting() {
        RongPushPremissionsCheckHelper.checkPermissionsAndShowDialog(this, new ResultCallback() {
            @Override
            public void onAreadlyOpened(String value) {
                LogCat.e(TAG, "11111 onAreadlyOpened  value=" + value);
            }

            @Override
            public boolean onBeforeShowDialog(String value) {
                LogCat.e(TAG, "11111 onBeforeShowDialog value=" + value);
                return false;
            }

            @Override
            public void onGoToSetting(String value) {
                LogCat.e(TAG, "11111 onGoToSetting value=" + value);
            }

            @Override
            public void onFailed(String value, FailedType type) {
                LogCat.e(TAG, "11111 onFailed value=" + value);
            }
        });
    }

    /**
     * 测试登录
     */
    public void testDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_test_login, null);
        dialog.setContentView(viewLayout);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = 900;
            dialogWindow.setAttributes(lp);
            ClearableEditText cetInfact = viewLayout.findViewById(R.id.cet_infact);
            ClearableEditText cetUrl = viewLayout.findViewById(R.id.cet_url);
            ClearableEditText cetTest = viewLayout.findViewById(R.id.cet_test);
            ClearableEditText cetCode = viewLayout.findViewById(R.id.cet_code);

            Button btnGo = viewLayout.findViewById(R.id.btn_go);
            btnGo.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(cetInfact.getText().toString().trim())) {
                    AppConfig.APP_URL = cetInfact.getText().toString().trim() + "/";
                }
                if (!TextUtils.isEmpty(cetUrl.getText().toString().trim())) {
                    AppConfig.APP_URL_MAIN = cetUrl.getText().toString().trim() + "/";
                }
                mPresenter.login(cetTest.getText().toString().trim(), cetCode.getText().toString().trim());
                dialog.dismiss();
            });
            dialog.setCancelable(true);
            dialog.show();
        }
    }
}
