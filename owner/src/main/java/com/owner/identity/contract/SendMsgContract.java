package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
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

    }

    interface Presenter {

        void getDetails(int id);
    }
}
