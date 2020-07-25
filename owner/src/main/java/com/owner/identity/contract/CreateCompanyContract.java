package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.identity.model.ApplyJoinBean;
import com.owner.identity.model.ApplyLicenceBean;
import com.owner.identity.model.GetIdentityInfoBean;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface CreateCompanyContract {
    interface View extends BaseView {

        void getIdentityInfoSuccess(GetIdentityInfoBean data);

        void submitSuccess();
    }

    interface Presenter {

        void getIdentityInfo(int identityType);

        void submitCompany(GetIdentityInfoBean data, int createCompany, int identityType,
                    String company, String address, String creditNo,
                    String mStrPath);

        void submitBuilding(GetIdentityInfoBean data, int createCompany, int identityType,
                            String buildingName, String address,
                            int district, int business, String mPath);

        void submitJointWork(GetIdentityInfoBean data, int createCompany, int identityType,
                             String branchesName, String address,
                             int district, int business, String mPath);
    }
}
