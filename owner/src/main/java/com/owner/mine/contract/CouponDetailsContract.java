package com.owner.mine.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.CouponDetailsBean;
import com.officego.commonlib.common.model.CouponWriteOffBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface CouponDetailsContract {
    interface View extends BaseView {

        void couponDetailsSuccess(CouponDetailsBean data);

        void writeOffSuccess(CouponWriteOffBean data);

        void writeOffFail();

    }

    interface Presenter {

        void getCouponDetails(String code);

        void sureWriteOff(List<CouponDetailsBean.BuildingMeetingroomListBean> arrayList,
                          int couponId, String roomName);
    }
}
