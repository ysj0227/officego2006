package com.officego.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.RegexUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.h5.WebViewActivity_;
import com.officego.ui.login.contract.LoginContract;
import com.officego.ui.login.presenter.LoginPresenter;
import com.officego.utils.GotoActivityUtils;
import com.officego.utils.MonitorEditTextUtils;
import com.owner.MainOwnerActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;
import java.util.Objects;

//import com.owner.MainOwnerActivity_;

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

    private String mobile;
    //判断是否从收藏或者个人中心未登录的时候进入
    @Extra
    boolean isGotoLogin;
    //业主model修改密码重新登录
    private boolean isOwnerLogin;
    //业主model修改密码重新登录,不清除栈顶
    private boolean isReOwnerLogin;
    /**
     * 倒计时对象,总共的时间,每隔多少秒更新一次时间
     */
    final MyCountDownTimer mTimer = new MyCountDownTimer(Constants.SMS_TIME, 1000);


    @AfterViews
    void init() {
        PermissionUtils.checkPermissionActivity(this);
        StatusBarUtils.setStatusBarColor(this);
        mPresenter = new LoginPresenter(context);
        mPresenter.attachView(this);
        btnLoginNoPassword.setVisibility(View.VISIBLE);
        new MonitorEditTextUtils(btnLogin, etMobile);
        tvProtocol.setText(Html.fromHtml(getString(R.string.str_click_login_agree_service)));
        if (getIntent().getExtras() != null) {
            isOwnerLogin = getIntent().getExtras().getBoolean("isOwnerLogin");
            isReOwnerLogin = getIntent().getExtras().getBoolean("isReOwnerLogin");
            rlBack.setVisibility(isOwnerLogin ? View.GONE : View.VISIBLE);
        }
    }

    @Click(R.id.btn_login)
    void loginClick() {
        if (isFastClick(1500)) {
            return;
        }
        mobile = RegexUtils.handleIllegalCharacter(etMobile.getText().toString().trim());
        if (rlCode.isShown()) {
            String code = Objects.requireNonNull(etCode.getText()).toString().trim();
            if (TextUtils.isEmpty(code)) {
                shortTip(R.string.str_please_input_sms_code);
                return;
            }
            //登录
            mPresenter.login(mobile, code);
        } else {
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
        if (isFastClick(1500)) {
            return;
        }
        //检查手机的权限设置
        if (PermissionUtils.checkPhonePermission(this)) {
            loginOnlyPhone();
        }
    }

    private void loginOnlyPhone() {
        if (!TextUtils.isEmpty(CommonHelper.getPhoneNum(context))) {
            mPresenter.loginOnlyPhone(CommonHelper.getPhoneNum(context));
        } else {
            shortTip(R.string.str_cannot_get_mine_phone);
        }
    }

    @Click(R.id.tv_wx_login)
    void testClick() {
        mPresenter.login("15981968964", "123465");
//        mPresenter.login("18237774543", "123465");
    }

    @Click(R.id.tv_get_code)
    void getCodeClick() {
        if (isFastClick(1200)) {
            return;
        }
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
    public void loginSuccess() {
        shortTip(R.string.str_login_success);
        if (TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRole())) {
            if (isReOwnerLogin) {
                //业主退出登录后的，重新登录
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
            } else if (isOwnerLogin) {
                MainOwnerActivity_.intent(context).start();
            } else {
                //业主首次登录跳转首页
                MainOwnerActivity_.intent(context).start();
            }
        }
        if (isGotoLogin) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        GotoActivityUtils.gotoHome(context);
    }
}
