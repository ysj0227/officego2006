package com.officego.ui.coupon.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.CouponListBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/10
 **/
public interface CouponListContract {

    interface View extends BaseView {

        void couponListSuccess(List<CouponListBean.ListBean> data);

        void endRefresh();

    }

    interface Presenter {
        void getCouponList(int status);
    }
}
