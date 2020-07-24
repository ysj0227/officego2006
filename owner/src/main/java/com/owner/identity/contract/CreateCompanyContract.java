package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.identity.model.ApplyJoinBean;
import com.owner.identity.model.ApplyLicenceBean;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface CreateCompanyContract {
    interface View extends BaseView {

        void getIdentityInfoSuccess(ApplyLicenceBean data);

        void sendApplySuccess(ApplyJoinBean data);
    }

    interface Presenter {

        void getIdentityInfo(int identityType, int id);

        void sendApply(int identityType, int id, int chattedId);
    }
}
