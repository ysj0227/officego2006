package com.officego.ui.mine.contract;

import com.officego.commonlib.base.BaseView;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface UpdateUserContract {
    interface View extends BaseView {

        void UpdateUserSuccess();

        void UpdateAvatarSuccess(String avatar);
    }

    interface Presenter {

        void updateAvatar(String path);

        void updateUserInfo(String avatar,String realName, String sex,String wx);
    }
}
