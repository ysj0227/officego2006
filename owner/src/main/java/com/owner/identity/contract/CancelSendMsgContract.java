package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.identity.model.CancelSendMsgBean;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface CancelSendMsgContract {
    interface View extends BaseView {

        void identityInfoSuccess(CancelSendMsgBean data);

        void cancelApplySuccess();

    }

    interface Presenter {

        void getIdentityInfo(int id);

        void cancelApply(int id);
    }
}
