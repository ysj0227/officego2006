package com.officego.ui.mine.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.ui.mine.model.UserBean;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface UserContract {
    interface View extends BaseView {

        void userInfoSuccess(UserMessageBean data);

    }

    interface Presenter {

        void getUserInfo();
    }
}
