package com.owner.mine.contract;

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

        void UpdateUserFail(int code, String msg);
    }

    interface Presenter {

        void updateAvatar(String path);

        void UpdateUserInfo(String avatar, String nickname, String sex, String job, String wx);
    }
}
