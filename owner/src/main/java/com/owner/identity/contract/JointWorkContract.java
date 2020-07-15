package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.identity.model.IdentityJointWorkBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface JointWorkContract {
    interface View extends BaseView {

        void searchJointWorkSuccess(List<IdentityJointWorkBean.DataBean> data);

        void searchCompanySuccess(List<IdentityCompanyBean.DataBean> data);

        void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data);

    }

    interface Presenter {

        void getJointWork(String keyword);

        void getCompany(String keyword);

        void getBuilding(String keyword);
    }
}
