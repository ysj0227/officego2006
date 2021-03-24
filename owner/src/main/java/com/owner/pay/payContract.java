package com.owner.pay;

import com.officego.commonlib.base.BaseView;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface payContract {
    interface View extends BaseView {
        void alipayResult(String orderInfo);
    }

    interface Presenter {
        void weChatPay(String amount, int buildingId);

        void alipay(String amount, int buildingId);
    }
}
