package com.officego.ui.login.presenter;

import android.content.Context;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rongcloud.ConnectRongCloudUtils;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.login.contract.LoginContract;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    /**
     * 获取验证码
     *
     * @param mobile
     */
    @Override
    public void sendSmsCode(String mobile) {
        OfficegoApi.getInstance().getSmsCode(mobile, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.sendSmsSuccess();
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                LogCat.e(TAG, "getSmsCode onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                }
            }
        });
    }

    @Override
    public void login(String mobile, String code) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().login(context, mobile, code, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                if (isViewAttached()) {
                    BaseNotification.newInstance().postNotificationName(
                            CommonNotifications.loginIn, "loginIn");
                    SpUtils.saveLoginInfo(data, mobile);
                    new ConnectRongCloudUtils();
                    mView.hideLoadingDialog();
                    mView.loginSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                if (isViewAttached()) {
                    mView.loginFail(code, msg);
                    mView.hideLoadingDialog();
                }
            }
        });
    }

    /**
     * 手机免密登录
     *
     * @param mobile mobile
     */
    @Override
    public void loginOnlyPhone(String mobile) {
        OfficegoApi.getInstance().loginOnlyPhone(context, mobile, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                LogCat.e(TAG, "loginOnlyPhone onSuccess =" + data);
                if (isViewAttached()) {
                    BaseNotification.newInstance().postNotificationName(
                            CommonNotifications.loginIn, "loginIn");
                    SpUtils.saveLoginInfo(data, mobile);
                    new ConnectRongCloudUtils();
                    mView.loginSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                LogCat.e(TAG, "loginOnlyPhone onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.loginFail(code, msg);
                }
            }
        });
    }
}
