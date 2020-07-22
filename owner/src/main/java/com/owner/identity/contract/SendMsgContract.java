package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.identity.model.ApplyJoinBean;
import com.owner.identity.model.ApplyLicenceBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface SendMsgContract {
    interface View extends BaseView {

        void messageSuccess(ApplyLicenceBean data);

        void sendApplySuccess(ApplyJoinBean data);

        void cancelApplySuccess();

    }

    interface Presenter {

        void getDetails(int identityType, int id);

        void sendApply(int identityType, int id,int chattedId);

        void cancelApply(int id);
    }
}
