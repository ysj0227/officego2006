package com.owner.mine.contract;

import com.officego.commonlib.base.BaseView;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface ModifyMobileContract {
    interface View extends BaseView {

//        void getSmsSuccess();

        void modifyMobileSuccess();

//        void modifyMobileFail(int code, String msg);
    }

    interface Presenter {

        void getSms(String mobile);

        void modifyMobile(String mobile, String code);
    }
}
