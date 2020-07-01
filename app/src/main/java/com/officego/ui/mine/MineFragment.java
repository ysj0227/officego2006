package com.officego.ui.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rongcloud.RongCloudSetUserInfoUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.CircleImage;
import com.officego.h5.WebViewActivity_;
import com.officego.ui.login.LoginActivity_;
import com.officego.ui.mine.contract.UserContract;
import com.officego.ui.mine.model.UserBean;
import com.officego.ui.mine.presenter.UserPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import static android.app.Activity.RESULT_OK;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@EFragment(R.layout.mine_fragment)
public class MineFragment extends BaseMvpFragment<UserPresenter>
        implements UserContract.View {
    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_CODE_LOGOUT = 1009;
    private static final int REQUEST_CODE_LOGIN = 1010;
    @ViewById(R.id.civ_avatar)
    CircleImage civAvatar;
    @ViewById(R.id.tv_name)
    TextView tvName;
    @ViewById(R.id.tv_account)
    TextView tvAccount;
    @ViewById(R.id.btn_login)
    Button btnLogin;

    private UserBean mUserInfo;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new UserPresenter();
        mPresenter.attachView(this);
        //当前未登录状态
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            noLoginView();
        } else {
            hasLoginView();
            mPresenter.getUserInfo();
        }
    }

    private boolean isToLogin() {
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            LoginActivity_.intent(mActivity).isGotoLogin(true).startForResult(REQUEST_CODE_LOGIN);
            return true;
        }
        return false;
    }

    @Click(R.id.btn_login)
    void loginClick() {
        if (isFastClick(1500)) {
            return;
        }
        LoginActivity_.intent(mActivity).isGotoLogin(true).startForResult(REQUEST_CODE_LOGIN);
    }

    @OnActivityResult(REQUEST_CODE_LOGIN)
    void onLoginResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mPresenter.getUserInfo();
        }
    }

    @Click(R.id.iv_setting)
    void settingClick() {
        if (isToLogin()) return;
        MineSettingActivity_.intent(mActivity).startForResult(REQUEST_CODE_LOGOUT);
    }

    @OnActivityResult(REQUEST_CODE_LOGOUT)
    void onLogoutResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            noLoginView();
        }
    }

    @Click(R.id.civ_avatar)
    void editMessageClick() {
        if (isToLogin()) return;
        if (mUserInfo == null) {
            mPresenter.getUserInfo();
            return;
        }
        MineMessageActivity_.intent(mActivity).mUserInfo(mUserInfo).startForResult(REQUEST_CODE);
    }

    @Click(R.id.tv_name)
    void nameClick() {
        if (isToLogin()) return;
        if (mUserInfo == null) {
            mPresenter.getUserInfo();
            return;
        }
        MineMessageActivity_.intent(mActivity).mUserInfo(mUserInfo).startForResult(REQUEST_CODE);
    }

    @OnActivityResult(REQUEST_CODE)
    void onAvatarResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mPresenter.getUserInfo();
            String avatarUrl = data.getStringExtra("avatarUrl");
            if (!TextUtils.isEmpty(avatarUrl)) {
                Glide.with(mActivity).load(avatarUrl).into(civAvatar);
            }
        }
    }

    @Click(R.id.rl_viewing_date)
    void viewDateClick() {
        if (isToLogin()) return;
        ViewingDateActivity_.intent(mActivity).start();
    }

    @Click(R.id.rl_help)
    void helpClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_HELP).start();
    }

    @Click(R.id.rl_service)
    void serviceClick() {
        ServiceActivity_.intent(mActivity).start();
    }

    @Click(R.id.rl_protocol)
    void protocolClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_PROTOCOL).start();
    }

    @Click(R.id.rl_about)
    void aboutClick() {
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {

            return;
        }
        WebViewActivity_.intent(mActivity).flags(Constants.H5_ABOUTS).start();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void userInfoSuccess(UserBean data) {
        hasLoginView();
        //刷新融云头像用户信息
        mUserInfo = data;
        if (data != null) {
            RongCloudSetUserInfoUtils.refreshUserInfoCache(RongCloudSetUserInfoUtils.getRongTenantId(data.getUserId() + ""), data.getRealname(), data.getAvatar());
            SpUtils.saveWechat(mUserInfo.getWxId() == null || TextUtils.isEmpty((String) mUserInfo.getWxId()) ? "" : (String) mUserInfo.getWxId());
            Glide.with(mActivity).applyDefaultRequestOptions(GlideUtils.avaOoptions()).load(data.getAvatar()).into(civAvatar);
            tvName.setText(data.getRealname());
            if (TextUtils.isEmpty((String) data.getCompany())) {
                tvAccount.setText(TextUtils.isEmpty((String) data.getJob()) ? "" : (String) data.getJob());
            } else {
                tvAccount.setText(data.getCompany() + "·" + (TextUtils.isEmpty((String) data.getJob()) ? "" : (String) data.getJob()));
            }
        }
    }

    @Override
    public void userInfoFail(int code, String msg) {
//        shortTip(R.string.tip_get_person_exception);
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.updateUserInfoSuccess};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (args == null) {
            return;
        }
        if (id == CommonNotifications.updateUserInfoSuccess) {
            mPresenter.getUserInfo();
        }
    }

    private void noLoginView() {
        tvName.setText("未登录");
        tvAccount.setText("登录开启所有精彩");
        btnLogin.setVisibility(View.VISIBLE);
        Glide.with(mActivity).load("").error(ContextCompat.getDrawable(mActivity, R.mipmap.default_avatar)).into(civAvatar);
    }

    private void hasLoginView() {
        tvName.setText("");
        tvAccount.setText("");
        btnLogin.setVisibility(View.GONE);
    }
}
