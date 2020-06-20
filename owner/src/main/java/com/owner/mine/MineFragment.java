package com.owner.mine;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rongcloud.RongCloudSetUserInfoUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.CircleImage;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.h5.WebViewActivity_;
import com.owner.mine.contract.UserContract;
import com.owner.mine.model.UserOwnerBean;
import com.owner.mine.presenter.UserPresenter;
import com.owner.utils.GotoActivityUtils;

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
@EFragment(resName = "mine_owner_fragment")
public class MineFragment extends BaseMvpFragment<UserPresenter>
        implements UserContract.View {
    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_CODE_LOGOUT = 1009;
    private static final int REQUEST_CODE_LOGIN = 1010;
    @ViewById(resName = "civ_avatar")
    CircleImage civAvatar;
    @ViewById(resName = "tv_name")
    TextView tvName;
    @ViewById(resName = "tv_account")
    TextView tvAccount;
    @ViewById(resName = "tv_idify")
    TextView tvIdify;
    @ViewById(resName = "btn_login")
    Button btnLogin;

    private UserOwnerBean mUserInfo;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new UserPresenter();
        mPresenter.attachView(this);
        mPresenter.getUserInfo();
        //当前未登录状态
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            noLoginView();
        } else {
            hasLoginView();
            mPresenter.getUserInfo();
        }
    }

    @Click(resName = "iv_setting")
    void settingClick() {
        if (isFastClick(1500)) {
            return;
        }
        MineSettingActivity_.intent(mActivity).startForResult(REQUEST_CODE_LOGOUT);
    }

    @OnActivityResult(REQUEST_CODE_LOGOUT)
    void onLogoutResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            noLoginView();
        }
    }

    @Click(resName = "btn_login")
    void loginClick() {
        if (isFastClick(1500)) {
            return;
        }
        GotoActivityUtils.loginActivity(mActivity, REQUEST_CODE_LOGIN);
    }

    @OnActivityResult(REQUEST_CODE_LOGIN)
    void onLoginResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mPresenter.getUserInfo();
        }
    }

    @Click(resName = "civ_avatar")
    void editMessageClick() {
        if (mUserInfo == null) {
            mPresenter.getUserInfo();
            return;
        }
        if (mUserInfo.getAuditStatus() != 1) {
            shortTip("信息未认证，请您先认证信息");
            return;
        }
        MineMessageActivity_.intent(mActivity).mUserInfo(mUserInfo).startForResult(REQUEST_CODE);
    }

    @Click(resName = "tv_name")
    void nameClick() {
        if (mUserInfo == null) {
            mPresenter.getUserInfo();
            return;
        }
        if (mUserInfo.getAuditStatus() != 0 && mUserInfo.getAuditStatus() != 1) {
            shortTip("信息未认证，请您先认证信息");
            return;
        }
        MineMessageActivity_.intent(mActivity).mUserInfo(mUserInfo).startForResult(REQUEST_CODE);
    }

    @OnActivityResult(REQUEST_CODE)
    void onAvatarResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String avatarUrl = data.getStringExtra("avatarUrl");
            if (!TextUtils.isEmpty(avatarUrl)) {
                Glide.with(mActivity).load(avatarUrl).into(civAvatar);
            }
        }
    }

    @Click(resName = "rl_help")
    void helpClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_HELP).start();
    }

    @Click(resName = "rl_service")
    void serviceClick() {
        callPhone("15981968964");
    }

    @Click(resName = "rl_protocol")
    void protocolClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_PROTOCOL).start();
    }

    @Click(resName = "rl_about")
    void aboutClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_ABOUTS).start();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.updateUserOwnerInfoSuccess};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (args == null) {
            return;
        }
        if (id == CommonNotifications.updateUserOwnerInfoSuccess) {
            mPresenter.getUserInfo();
        }
    }

    @Override
    public void userInfoSuccess(UserOwnerBean data) {
        hasLoginView();
        if (data != null) {
            //刷新融云头像用户信息
            RongCloudSetUserInfoUtils.refreshUserInfoCache(SpUtils.getRongChatId(), data.getRealname(), data.getAvatar());
            mUserInfo = data;
            tvIdify.setText(idify(data));
            Glide.with(mActivity).load(data.getAvatar()).into(civAvatar);
            tvName.setText(data.getRealname());
            if (TextUtils.isEmpty(data.getProprietorCompany())) {
                tvAccount.setText(TextUtils.isEmpty(data.getProprietorJob()) ? "" : data.getProprietorJob());
            } else {
                tvAccount.setText(data.getProprietorCompany() + "·" + (TextUtils.isEmpty(data.getProprietorJob()) ? "" : data.getProprietorJob()));
            }
            unIdifyDialog(data);
        }
    }

    private CommonDialog dialog;

    private void unIdifyDialog(UserOwnerBean data) {
        // 0待审核1审核通过2审核未通过
        if (dialog == null && data.getAuditStatus() != 0 && data.getAuditStatus() != 1) {
            dialog = new CommonDialog.Builder(mActivity)
                    .setTitle(idify(data) + "\n请您先认证信息")
                    .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                        shortTip("跳转H5认证");
                        dialog.dismiss();
                        dialog = null;
                    }).create();
            dialog.showWithOutTouchable(false);
        }
    }

    /**
     * identityType :  "identityType": 0,//身份类型0个人1企业2联合
     * auditStatus :  0待审核1审核通过2审核未通过
     */
    private String idify(UserOwnerBean data) {
        String id, status;
        if (data.getIdentityType() == 0) {
            id = "个人";
        } else if (data.getIdentityType() == 1) {
            id = "企业";
        } else {
            id = "联合办公";
        }
        if (data.getAuditStatus() == 1) {
            status = "已认证";
        } else {
            status = "未认证";
        }
        return id + "-" + status;
    }

    @Override
    public void userInfoFail(int code, String msg) {

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
