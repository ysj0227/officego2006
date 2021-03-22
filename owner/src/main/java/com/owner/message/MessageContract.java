package com.owner.message;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.common.model.owner.BuildingJointWorkBean;
import com.officego.commonlib.common.model.owner.HouseBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface MessageContract {
    interface View extends BaseView {

        void userInfoSuccess(UserMessageBean data);

    }

    interface Presenter {
        void getUserInfo();
    }
}
