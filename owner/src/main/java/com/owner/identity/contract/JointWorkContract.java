package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.identity.model.GetIdentityInfoBean;
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

        void checkCompanyInfoSuccess();

        void checkJointWorkInfoSuccess();

        void getIdentityInfoSuccess(GetIdentityInfoBean data, boolean isFirstGetInfo);

        void submitSuccess();
    }

    interface Presenter {
        void checkCompany(int identityType, String name);

        void checkJointWork(int identityType, String name);

        void getJointWork(String keyword);

        void getCompany(String keyword);

        void getBuilding(String keyword);

        void getIdentityInfo(int identityType, boolean isFirstGetInfo);

        void submit(GetIdentityInfoBean data, int createCompany, int identityType, int leaseType,
                    String buildingName, List<String> mFilePremisesPath, List<String> mFileContractPath);
    }
}
