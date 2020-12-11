package com.officego.ui.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.officego.MainOwnerActivity_;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.common.rongcloud.ConnectRongCloudUtils;
import com.officego.commonlib.common.rongcloud.RongCloudSetUserInfoUtils;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.CircleImage;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.h5.WebViewActivity_;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.coupon.CouponActivity_;
import com.officego.ui.login.LoginActivity_;
import com.officego.ui.mine.contract.UserContract;
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
@SuppressLint("NewApi")
@EFragment(R.layout.mine_fragment)
public class MineFragment extends BaseMvpFragment<UserPresenter>
        implements UserContract.View {
    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_CODE_LOGOUT = 1009;
    @ViewById(R.id.civ_avatar)
    CircleImage civAvatar;
    @ViewById(R.id.tv_name)
    TextView tvName;
    @ViewById(R.id.tv_account)
    TextView tvAccount;
    @ViewById(R.id.btn_login)
    Button btnLogin;

    private UserMessageBean mUserInfo;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new UserPresenter();
        mPresenter.attachView(this);
        //版本更新
        new VersionDialog(mActivity);
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
            LoginActivity_.intent(mActivity).start();
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        //未登录
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            noLoginView();
        }
    }

    @Click(R.id.btn_login)
    void loginClick() {
        if (isFastClick(1500)) {
            return;
        }
        //神策
        SensorsTrack.login();
        //登录
        LoginActivity_.intent(mActivity).start();
    }

    @Click(R.id.iv_setting)
    void settingClick() {
        if (isToLogin()) return;
        MineSettingActivity_.intent(mActivity).startForResult(REQUEST_CODE_LOGOUT);
    }

    @Click(R.id.rl_coupon)
    void couponClick() {
        if (isToLogin()) return;
        CouponActivity_.intent(mActivity).start();
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

    @Click(R.id.rl_protocol_service)
    void protocolServiceClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_PROTOCOL_SERVICE).start();
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
        WebViewActivity_.intent(mActivity).flags(Constants.H5_ABOUTS).start();
    }

    @Click(R.id.rl_switch_id)
    void switchClick() {
        if (isToLogin()) return;
        switchDialog();
    }

    private void switchDialog() {
        CommonDialog dialog = new CommonDialog.Builder(mActivity)
                .setTitle(R.string.are_you_sure_switch_owner)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    switchId(Constants.TYPE_OWNER);
                    //神策
                    SensorsTrack.tenantToOwner();
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    //租户端---用户身份标：0租户，1户主
    private void switchId(String role) {
        showLoadingDialog();
        OfficegoApi.getInstance().switchId(role, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                hideLoadingDialog();
                SpUtils.saveLoginInfo(data, SpUtils.getPhoneNum());
                SpUtils.saveRole(String.valueOf(data.getRid()));
                new ConnectRongCloudUtils();//连接融云
                if (TextUtils.equals(Constants.TYPE_TENANT, String.valueOf(data.getRid()))) {
                    GotoActivityUtils.mainActivity(mActivity); //跳转租户首页
                } else if (TextUtils.equals(Constants.TYPE_OWNER, String.valueOf(data.getRid()))) {
                    MainOwnerActivity_.intent(mActivity).start(); //租户切换房东
                }
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                hideLoadingDialog();
                if (code == Constants.ERROR_CODE_5009) {
                    shortTip(msg);
                    SpUtils.clearLoginInfo();
                    GotoActivityUtils.loginClearActivity(mActivity, false);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void userInfoSuccess(UserMessageBean data) {
        hasLoginView();
        if (data != null) {
            //刷新融云头像用户信息
            RongCloudSetUserInfoUtils.refreshUserInfoCache(SpUtils.getRongChatId(), data.getNickname(), data.getAvatar());
            mUserInfo = data;
            tvName.setText(data.getNickname());
            SpUtils.saveWechat(mUserInfo.getWxId() == null || TextUtils.isEmpty(mUserInfo.getWxId()) ? "" : mUserInfo.getWxId());
            Glide.with(mActivity).applyDefaultRequestOptions(GlideUtils.avaOoptions()).load(data.getAvatar()).into(civAvatar);
            if (!TextUtils.isEmpty(data.getJob())) {
                tvAccount.setText(data.getJob());
            }
        }
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
