package com.owner.mine.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.UserMessageBean;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface UserContract {
    interface View extends BaseView {

        void userInfoSuccess(UserMessageBean data);

        void userInfoFail(int code, String msg);
    }

    interface Presenter {

        void getUserInfo();
    }
}
