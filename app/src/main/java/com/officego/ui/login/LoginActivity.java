package com.officego.ui.login;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
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
import com.officego.commonlib.common.analytics.GoogleTrack;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.NotificationUtil;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.RegexUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.h5.WebViewActivity_;
import com.officego.ui.login.contract.LoginContract;
import com.officego.ui.login.presenter.LoginPresenter;
import com.officego.utils.JPushAuthLoginRequest;
import com.officego.utils.MonitorEditTextUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;
import java.util.Objects;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/

@SuppressLint({"Registered", "NonConstantResourceId"})
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
    @Extra
    boolean isFinishCurrentView; //关闭当前租户页面
    private String mobile;
    //倒计时对象,总共的时间,每隔多少秒更新一次时间
    final MyCountDownTimer mTimer = new MyCountDownTimer(Constants.SMS_TIME, 1000);

    @AfterViews
    void init() {
        PermissionUtils.checkPermissionActivity(this);
        channelTrack();
        StatusBarUtils.setStatusBarColor(this);
        mPresenter = new LoginPresenter(context);
        mPresenter.attachView(this);
        initViews();
        SpUtils.saveImei(context);
    }

    private void initViews() {
        new MonitorEditTextUtils(btnLogin, etMobile);
        tvProtocol.setText(Html.fromHtml(getString(R.string.str_click_login_agree_service)));
        if (getIntent().getExtras() != null) { //房东model修改密码重新登录
            boolean isOwnerLogin = getIntent().getExtras().getBoolean("isOwnerLogin");
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
    }

    //点击验证码输入框
    @FocusChange(R.id.et_code)
    void codeFocusChange(View view, boolean b) {
        SensorsTrack.codeInput();//神策
    }

    @Click(R.id.btn_login)
    void loginClick() {
        if (isFastClick(1500)) {
            return;
        }
        GoogleTrack.login(context);
        mobile = RegexUtils.handleIllegalCharacter(etMobile.getText() == null ? "" :
                etMobile.getText().toString().trim());
        if (rlCode.isShown()) {
            SensorsTrack.login(); //神策
            String code = Objects.requireNonNull(etCode.getText()).toString().trim();
            if (TextUtils.isEmpty(code)) {
                shortTip(R.string.str_please_input_sms_code);
                return;
            }
            mPresenter.login(mobile, code); //登录
        } else {
            SensorsTrack.smsCode(); //神策
            if (!RegexUtils.isChinaPhone(mobile)) {
                shortTip(R.string.tip_input_correct_phone);
                return;
            }
            rlCode.setVisibility(View.VISIBLE);
            btnLogin.setText(R.string.str_login);
            startDownTimer(mobile); //发送验证码
        }
    }

    /**
     * 本机免密登录
     */
    @Click(R.id.btn_login_no_password)
    void loginNoPasswordClick() {
        if (isFastClick(1500)) {
            return;
        }
        GoogleTrack.keyLogin(context);
        SensorsTrack.login();//神策
        if (PermissionUtils.checkPhonePermission(this)) {
            JPushAuthLoginRequest.getInstance().authLogin(context);
        }
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

    //登录
    @Click(R.id.tv_protocol)
    void protocolClick() {
        if (isFastClick(1200)) {
            return;
        }
        WebViewActivity_.intent(context).flags(Constants.H5_PROTOCOL_SERVICE).start();
    }

    @Click(R.id.tv_privacy)
    void protocolPrivacyClick() {
        if (isFastClick(1200)) {
            return;
        }
        WebViewActivity_.intent(context).flags(Constants.H5_PROTOCOL).start();
    }

    @Click(R.id.btn_test)
    void testClick() {
        //new TestLoginDialog(context,mPresenter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SensorsTrack.trackInstallation(context);
        }
        if (grantResults.length <= 0) {
            return;
        }
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//用户拒绝
            shortTip(R.string.tip_permission_ungranted);
        }
    }

    @Override
    public void loginSuccess(LoginBean data) {
        //google用户id
        GoogleTrack.setUserId(context);
        //神策
        SensorsTrack.sensorsLogin(data.getUid());
        //登录成功跳转
        String rid = String.valueOf(data.getRid());
        //初始化未登录时，当前身份身份有变化
        if (isFinishCurrentView && TextUtils.equals(SpUtils.getRole(), rid)) {
            SpUtils.saveRole(rid);
        } else {
            SpUtils.saveRole(rid);
            if (TextUtils.equals(Constants.TYPE_OWNER, String.valueOf(data.getRid()))) {
                MainOwnerActivity_.intent(context).start();
            } else {
                MainActivity_.intent(context).start();
            }
        }
        finish();
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.JPushSendPhone};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (id == CommonNotifications.JPushSendPhone) {
            mPresenter.getJPushPhone((String) args[0]);
        }
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
            }
        } else {
            SensorsTrack.trackInstallation(context);
        }
    }

}
