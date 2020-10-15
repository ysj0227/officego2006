package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.mine.model.UserOwnerBean;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface HomeContract {
    interface View extends BaseView {

        void userInfoSuccess(UserOwnerBean data);

    }

    interface Presenter {

        void getUserInfo();
    }
}
