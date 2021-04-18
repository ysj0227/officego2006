package com.officego.ui.login.presenter;

import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.JPushLoginBean;
import com.officego.commonlib.common.model.LoginBean;
import com.officego.commonlib.common.model.WeChatAuthBean;
import com.officego.commonlib.common.rongcloud.RCloudConnectUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.retrofit.RxJavaCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.login.contract.LoginContract;

import io.reactivex.disposables.Disposable;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    /**
     * 获取验证码
     */
    @Override
    public void sendSmsCode(String mobile) {
        OfficegoApi.getInstance().getSmsCode(mobile, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
            }

            @Override
            public void onFail(int code, String msg, Object data) {
            }
        });
    }

    @Override
    public void login(String mobile, String code) {
//        mView.showLoadingDialog();
//        OfficegoApi.getInstance().login(mobile, code, new RetrofitCallback<LoginBean>() {
//            @Override
//            public void onSuccess(int code, String msg, LoginBean data) {
//                if (isViewAttached()) {
//                    loginSuccess(mobile, data);
//                }
//            }
//
//            @Override
//            public void onFail(int code, String msg, LoginBean data) {
//                if (isViewAttached()) {
//                    mView.hideLoadingDialog();
//                    if (Constants.DEFAULT_ERROR_CODE == code) {
//                        mView.shortTip(msg);
//                    }
//                }
//            }
//        });
        OfficegoApi.getInstance().rxLogin(mobile, code, new RxJavaCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                LogCat.e("TAG","1111="+data.getPhone()+","+data.getNickName());
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {

            }

            @Override
            public void onSubscribe(Disposable d) {

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
        mView.showLoadingDialog();
        OfficegoApi.getInstance().loginOnlyPhone(mobile, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                if (isViewAttached()) {
                    loginSuccess(mobile, data);
                }
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (Constants.DEFAULT_ERROR_CODE == code) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    private void loginSuccess(String mobile, LoginBean data) {
        mView.hideLoadingDialog();
        saveWantFind();
        BaseNotification.newInstance().postNotificationName(CommonNotifications.loginIn, "loginIn");
        SpUtils.saveLoginInfo(data, mobile);
        new RCloudConnectUtils();
        mView.loginSuccess(data);
    }

    /**
     * 保存我想找
     */
    public void saveWantFind() {
        if (TextUtils.isEmpty(SpUtils.getImei()) &&
                TextUtils.isEmpty(SpUtils.getLoginWantFind()) &&
                !TextUtils.isEmpty(SpUtils.getWantFind())) {
            OfficegoApi.getInstance().wantToFind(SpUtils.getWantFindPerson(), SpUtils.getWantFindRent(),
                    SpUtils.getWantFindFactor(), new RetrofitCallback<Object>() {
                        @Override
                        public void onSuccess(int code, String msg, Object data) {
                            SpUtils.saveLoginWantFind();
                        }

                        @Override
                        public void onFail(int code, String msg, Object data) {
                        }
                    });
        }
    }

    /**
     * 一键登录 获取手机号
     */
    @Override
    public void getJPushPhone(String loginToken) {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().jPushLogin(
                loginToken, new RetrofitCallback<JPushLoginBean>() {
                    @Override
                    public void onSuccess(int code, String msg, JPushLoginBean data) {
                        if (isViewAttached()) {
                            loginOnlyPhone(data.getPhone());//免密登录
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, JPushLoginBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }


    /**
     * 微信授权信息
     */
    @Override
    public void weChatAuthInfo(String code) {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().weChatAuthInfo(
                code, new RetrofitCallback<WeChatAuthBean>() {
                    @Override
                    public void onSuccess(int code, String msg, WeChatAuthBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            weChatBindPhoneCheck(data, "", "");//授权后检验
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, WeChatAuthBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }

    /**
     * 微信是否绑定手机校验
     */
    @Override
    public void weChatBindPhoneCheck(WeChatAuthBean data, String phone, String smsCode) {
        if (data != null) {
            mView.showLoadingDialog();
            com.officego.commonlib.common.rpc.OfficegoApi.getInstance().weChatBindPhoneCheck(
                    data.getOpenid(), data.getUnionid(), data.getAccessToken(), data.getNickname(),
                    data.getHeadimgurl(), data.getSex(), phone, smsCode, new RetrofitCallback<LoginBean>() {
                        @Override
                        public void onSuccess(int code, String msg, LoginBean loginBean) {
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                                loginSuccess(loginBean.getPhone(), loginBean);
                            }
                        }

                        @Override
                        public void onFail(int code, String msg, LoginBean loginBean) {
                            if (isViewAttached()) {
                                mView.hideLoadingDialog();
                                if (code == Constants.DEFAULT_ERROR_CODE) {
                                    mView.shortTip(msg);
                                } else if (code == Constants.ERROR_CODE_5011) {
                                    //微信未绑定手机   此时提示绑定手机号，再次进行微信校验登录
                                    mView.weChatBindPhone(data);
                                }
                            }
                        }
                    });
        }
    }
}
