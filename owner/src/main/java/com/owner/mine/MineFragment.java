package com.owner.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.model.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.analytics.GoogleTrack;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.common.rongcloud.RCloudConnectUtils;
import com.officego.commonlib.common.rongcloud.RCloudSetUserInfoUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.CircleImage;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.dialog.ExitAppDialog;
import com.owner.h5.WebViewActivity_;
import com.owner.mine.contract.UserContract;
import com.owner.mine.coupon.WriteOffRecordActivity_;
import com.owner.mine.presenter.UserPresenter;
import com.owner.rpc.OfficegoApi;
import com.owner.zxing.QRScanActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import static android.app.Activity.RESULT_OK;
import static com.officego.commonlib.utils.PermissionUtils.REQ_PERMISSIONS_CAMERA_STORAGE;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint("NewApi")
@EFragment(resName = "mine_owner_fragment")
public class MineFragment extends BaseMvpFragment<UserPresenter>
        implements UserContract.View {
    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_CODE_IDENTITY = 1010;
    @ViewById(resName = "civ_avatar")
    CircleImage civAvatar;
    @ViewById(resName = "tv_name")
    TextView tvName;
    @ViewById(resName = "tv_account")
    TextView tvAccount;
    @ViewById(resName = "tv_idify")
    TextView tvIdify;
    private UserMessageBean mUserInfo;

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

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new ExitAppDialog(mActivity);
        }
    }

    @Click(resName = "iv_setting")
    void settingClick() {
        if (isFastClick(1500)) {
            return;
        }
        MineSettingActivity_.intent(mActivity).start();
    }

    @Click(resName = "tv_idify")
    void idifyTextClick() {
        identityMessage();
    }

    @Click(resName = "civ_avatar")
    void editMessageClick() {
        identityMessage();
    }

    @Click(resName = "tv_name")
    void nameClick() {
        identityMessage();
    }

    private void identityMessage() {
        if (mUserInfo == null) {
            mPresenter.getUserInfo();
            return;
        }
        MineMessageActivity_.intent(mActivity).mUserInfo(mUserInfo).startForResult(REQUEST_CODE);
    }

    @OnActivityResult(REQUEST_CODE_IDENTITY)
    void onIdentityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mPresenter.getUserInfo();
        }
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

    @Click(resName = "rl_record")
    void recordClick() {
        WriteOffRecordActivity_.intent(mActivity).start();
    }

    @Click(resName = "rl_help")
    void helpClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_HELP).start();
    }

    @Click(resName = "rl_protocol_service")
    void protocolServiceClick() {
        WebViewActivity_.intent(mActivity).flags(Constants.H5_PROTOCOL_SERVICE).start();
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

    @Click(resName = "rl_switch_id")
    void switchClick() {
        GoogleTrack.switchId(mActivity);
        if (isFastClick(1000)) {
            return;
        }
        switchDialog();
    }

    //扫一扫
    @Click(resName = "iv_scan")
    void scanClick() {
//        scanDialog(getContext());
        startActivity(new Intent(getActivity(), QRScanActivity.class));
    }

    private void switchDialog() {
        CommonDialog dialog = new CommonDialog.Builder(mActivity)
                .setTitle(R.string.are_you_sure_switch_tenant)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    switchId(Constants.TYPE_TENANT);
                    //神策
                    SensorsTrack.ownerToTenant();
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    //房东端--- 用户身份标：0租户，1户主
    private void switchId(String role) {
        showLoadingDialog();
        OfficegoApi.getInstance().switchId(role, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                hideLoadingDialog();
                SpUtils.saveLoginInfo(data, SpUtils.getPhoneNum());
                SpUtils.saveRole(String.valueOf(data.getRid()));
                new RCloudConnectUtils();//连接融云
                if (TextUtils.equals(Constants.TYPE_TENANT, String.valueOf(data.getRid()))) {
                    GotoActivityUtils.mainActivity(mActivity); //跳转租户首页
                } else if (TextUtils.equals(Constants.TYPE_OWNER, String.valueOf(data.getRid()))) {
                    GotoActivityUtils.mainOwnerActivity(mActivity); //租户切换房东
                }
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                hideLoadingDialog();
                if (code == Constants.ERROR_CODE_5009) {
                    shortTip(msg);
                    SpUtils.clearLoginInfo();
                    GotoActivityUtils.loginClearActivity(mActivity, true);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void userInfoSuccess(UserMessageBean data) {
        if (data != null) {
            //刷新融云头像用户信息
            RCloudSetUserInfoUtils.refreshUserInfoCache(SpUtils.getRongChatId(), data.getNickname(), data.getAvatar());
            mUserInfo = data;
            tvName.setText(data.getNickname());
            Glide.with(mActivity).applyDefaultRequestOptions(GlideUtils.avaOoptions()).load(data.getAvatar()).into(civAvatar);
            if (!TextUtils.isEmpty(data.getJob())) {
                tvAccount.setText(data.getJob());
            }
        }
    }

    @Override
    public void userInfoFail(int code, String msg) {

    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.updateUserOwnerInfoSuccess};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
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

    //扫一扫
    private void scanDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_scan, null);
        dialog.setContentView(inflate);
        inflate.findViewById(R.id.btn_close).setOnClickListener(v -> dialog.dismiss());
        inflate.findViewById(R.id.btn_app).setOnClickListener(v -> dialog.dismiss());
        inflate.findViewById(R.id.btn_web).setOnClickListener(v -> {
            if (!fragmentCheckSDCardCameraPermission()) {
                return;
            }
            dialog.dismiss();
            startActivity(new Intent(getActivity(), QRScanActivity.class));
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
