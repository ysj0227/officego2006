package com.owner.zxing.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.mine.model.UserOwnerBean;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface ScanContract {
    interface View extends BaseView {

        void scanSuccess();

        void scanFail(int code, String msg);
    }

    interface Presenter {

        void scanLogin(String scanContent);
    }
}
