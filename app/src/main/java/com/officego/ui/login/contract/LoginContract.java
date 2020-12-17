package com.officego.ui.login.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.LoginBean;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface LoginContract {

    interface View extends BaseView {

        void loginSuccess(LoginBean data);

        void loginFail(int code, String msg);
    }

    interface Presenter {

        void sendSmsCode(String mobile);

        void login(String mobile, String password);

        void loginOnlyPhone(String mobile);

        void getJPushPhone(String loginToken);//一键登录极光登录token
    }
}
