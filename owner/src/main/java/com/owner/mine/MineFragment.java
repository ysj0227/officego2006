package com.owner.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rongcloud.RongCloudSetUserInfoUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.CircleImage;
import com.owner.h5.WebViewActivity_;
import com.owner.h5.WebViewIdifyActivity_;
import com.owner.identity.SelectIdActivity_;
import com.owner.mine.contract.UserContract;
import com.owner.mine.model.UserOwnerBean;
import com.owner.mine.presenter.UserPresenter;
import com.owner.utils.UnIdifyDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import static android.app.Activity.RESULT_OK;
import static com.officego.commonlib.utils.PermissionUtils.REQ_PERMISSIONS_CAMERA_STORAGE;

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
    private static final int REQUEST_CODE_IDENTITY = 1010;
    @ViewById(resName = "civ_avatar")
    CircleImage civAvatar;
    @ViewById(resName = "tv_name")
    TextView tvName;
    @ViewById(resName = "tv_account")
    TextView tvAccount;
    @ViewById(resName = "tv_idify")
    TextView tvIdify;
    @ViewById(resName = "btn_identity")
    Button btnIdentity;
    @ViewById(resName = "rl_role")
    RelativeLayout rlRole;

    private UserOwnerBean mUserInfo;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new UserPresenter();
        mPresenter.attachView(this);
        if (!fragmentCheckSDCardCameraPermission()) {
            return;
        }
        //版本更新
        new VersionDialog(mActivity);
        mPresenter.getUserInfo();
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
            //noIdentityView();
        }
    }

    /**
     * 去认证
     */
    @Click(resName = "btn_identity")
    void loginClick() {
        if (isFastClick(1500)) {
            return;
        }
        if (mUserInfo == null) {
            mPresenter.getUserInfo();
            return;
        }
        //当驳回时
        if (mUserInfo.getAuditStatus() == 2) {
            if (mUserInfo.getIdentityType() == 0) {//个人
                WebViewIdifyActivity_.intent(mActivity).idifyTag(Constants.H5_OWNER_IDIFY_PERSION).start();
            } else if (mUserInfo.getIdentityType() == 1) {//企业
                WebViewIdifyActivity_.intent(mActivity).idifyTag(Constants.H5_OWNER_IDIFY_COMPANY).start();
            } else if (mUserInfo.getIdentityType() == 2) { //联办
                WebViewIdifyActivity_.intent(mActivity).idifyTag(Constants.H5_OWNER_IDIFY_JOINTWORK).start();
            }
        } else { //当需要认证时
            WebViewIdifyActivity_.intent(mActivity).start();
        }
    }

    @Click(resName = "civ_avatar")
    void editMessageClick() {
        if (mUserInfo == null) {
            mPresenter.getUserInfo();
            return;
        }
        if (isIdentity()) {
            new UnIdifyDialog(mActivity, mUserInfo);
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
        if (isIdentity()) {
            new UnIdifyDialog(mActivity, mUserInfo);
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

    @Click(resName = "rl_role")
    void roleClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_ROLE).start();
    }

    @Click(resName = "rl_help")
    void helpClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_HELP).start();
    }

    @Click(resName = "rl_service")
    void serviceClick() {
        GotoActivityUtils.serviceActivity(mActivity);
    }

    @Click(resName = "rl_protocol")
    void protocolClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_PROTOCOL).start();
    }

    @Click(resName = "rl_about")
    void aboutClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_ABOUTS).start();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void userInfoSuccess(UserOwnerBean data) {
        if (data != null) {
            //刷新融云头像用户信息
            RongCloudSetUserInfoUtils.refreshUserInfoCache(SpUtils.getRongChatId(), data.getRealname(), data.getAvatar());
            mUserInfo = data;
            tvIdify.setText(idify(data));
            Glide.with(mActivity).applyDefaultRequestOptions(GlideUtils.avaOoptions()).load(data.getAvatar()).into(civAvatar);
            tvName.setText(data.getProprietorRealname());
            if (TextUtils.isEmpty(data.getProprietorCompany())) {
                tvAccount.setText(TextUtils.isEmpty(data.getProprietorJob()) ? "" : data.getProprietorJob());
            } else {
                tvAccount.setText(data.getProprietorCompany() + "·" + (TextUtils.isEmpty(data.getProprietorJob()) ? "" : data.getProprietorJob()));
            }
            if (isIdentity()) {
                noIdentityView();
                new UnIdifyDialog(mActivity, mUserInfo);
            } else {
                hasIdentityView();
            }
            //1企业2联合 &&管理员显示员工管理  权职0普通员工1管理员 -1无
            if ((data.getIdentityType() == 1 || data.getIdentityType() == 2)
                    && data.getAuthority() == 1 && data.getAuditStatus() == 1) {
                rlRole.setVisibility(View.VISIBLE);
            } else {
                rlRole.setVisibility(View.GONE);
            }
        }
    }

    // 0待审核1审核通过2审核未通过
    private boolean isIdentity() {
        if (mUserInfo != null) {
            return mUserInfo.getAuditStatus() != 0 && mUserInfo.getAuditStatus() != 1;
        }
        return false;
    }

    /**
     * identityType :  "identityType": 0,//身份类型0个人1企业2联合
     * auditStatus :  0待审核1审核通过2审核未通过 -1未认证
     */
    private String idify(UserOwnerBean data) {
        String id, status;
        if (data.getIdentityType() == 0) {
            id = "";
        } else if (data.getIdentityType() == 1) {
            id = "";
        } else if (data.getIdentityType() == 2) {
            id = "";
        } else {
            id = "";
        }
        if (data.getAuditStatus() == 0) {
            status = "待审核";
        } else if (data.getAuditStatus() == 1) {
            status = "已认证";
        } else if (data.getAuditStatus() == 2) {
            status = "审核未通过";
        } else {
            status = "未认证";
        }
        if (TextUtils.isEmpty(id)) {
            return status;
        }
        return id + status;
    }

    @Override
    public void userInfoFail(int code, String msg) {

    }

    @UiThread
    void noIdentityView() {
        btnIdentity.setVisibility(View.VISIBLE);
    }

    @UiThread
    void hasIdentityView() {
        btnIdentity.setVisibility(View.GONE);
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

    // SD卡,相机 fragment
    private boolean fragmentCheckSDCardCameraPermission() {
        //mActivity1 必须使用this 在fragment
        String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission1 = mActivity.checkSelfPermission(PERMISSIONS_STORAGE[0]);
            int permission2 = mActivity.checkSelfPermission(PERMISSIONS_STORAGE[1]);
            if (permission1 != PackageManager.PERMISSION_GRANTED ||
                    permission2 != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{
                        PERMISSIONS_STORAGE[0], PERMISSIONS_STORAGE[1]}, REQ_PERMISSIONS_CAMERA_STORAGE);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mPresenter.getUserInfo();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
