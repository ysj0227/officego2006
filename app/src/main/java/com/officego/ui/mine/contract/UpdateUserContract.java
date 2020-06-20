package com.officego.ui.mine.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.ui.mine.model.UserBean;

import java.io.File;

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

        void updateAvatar(File file);

        void UpdateUserInfo(String realName, String sex,String wx);
    }
}
