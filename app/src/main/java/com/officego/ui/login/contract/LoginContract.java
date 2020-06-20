package com.officego.ui.login.contract;

import com.officego.commonlib.base.BaseView;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface LoginContract {

    interface View extends BaseView {
        void sendSmsSuccess();

        void loginSuccess();

        void loginFail(int code, String msg);
    }

    interface Presenter {

        void sendSmsCode(String mobile);

        void login(String mobile, String password);

        void loginOnlyPhone(String mobile);
    }
}
