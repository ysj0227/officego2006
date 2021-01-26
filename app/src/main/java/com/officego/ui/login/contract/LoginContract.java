package com.officego.ui.login.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.LoginBean;
import com.officego.commonlib.common.model.WeChatAuthBean;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface LoginContract {

    interface View extends BaseView {

        void loginSuccess(LoginBean data);

        void weChatBindPhone(WeChatAuthBean data);
    }

    interface Presenter {

        void sendSmsCode(String mobile);

        void login(String mobile, String password);

        void loginOnlyPhone(String mobile);

        void getJPushPhone(String loginToken);//一键登录极光登录token

        void weChatAuthInfo(String code);//获取微信信息

        void weChatBindPhoneCheck(WeChatAuthBean data, String phone, String smsCode);//微信绑定手机
    }
}
