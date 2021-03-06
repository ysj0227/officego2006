package com.owner.mine.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.CouponWriteOffListBean;
import com.officego.commonlib.common.model.UserMessageBean;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface WriteOffContract {
    interface View extends BaseView {

        void writeOffListSuccess(CouponWriteOffListBean data);

        void endRefresh();
    }

    interface Presenter {

        void getWriteOffList(int pageNo);
    }
}
