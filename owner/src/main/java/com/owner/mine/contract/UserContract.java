package com.owner.mine.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.mine.model.UserOwnerBean;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface UserContract {
    interface View extends BaseView {

        void userInfoSuccess(UserOwnerBean data);

        void userInfoFail(int code, String msg);
    }

    interface Presenter {

        void getUserInfo();
    }
}
