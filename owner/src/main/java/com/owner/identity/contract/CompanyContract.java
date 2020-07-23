package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.identity.model.CheckIdentityBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface CompanyContract {
    interface View extends BaseView {

        void searchCompanySuccess(List<IdentityCompanyBean.DataBean> data);

        void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data);

        void checkCompanyInfoSuccess();

        void checkBuildingInfoSuccess();
    }

    interface Presenter {

        void getCompany(String keyword);

        void getBuilding(String keyword);

        void checkCompany(int identityType,String name);

        void checkBuilding(int identityType,String name);
    }
}
