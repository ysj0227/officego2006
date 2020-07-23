package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.QueryApplyLicenceBean;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface CancelSendMsgContract {
    interface View extends BaseView {

        void identityInfoSuccess(QueryApplyLicenceBean data);

        void cancelApplySuccess();

    }

    interface Presenter {

        void getIdentityInfo();

        void cancelApply(int id);
    }
}
