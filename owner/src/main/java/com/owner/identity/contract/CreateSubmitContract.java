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
public interface CreateSubmitContract {
    interface View extends BaseView {

        void getIdentityInfoSuccess(GetIdentityInfoBean data,boolean isFirstGetInfo);

        void districtListSuccess(String str);

        void submitSuccess();
    }

    interface Presenter {

        void getIdentityInfo(int identityType,boolean isFirstGetInfo);

        void getDistrictList(String district,String business);

        //创建公司提交
        void submitCompany(GetIdentityInfoBean data, int createCompany, int identityType,
                    String company, String address, String creditNo,
                    String mStrPath);
        //创建楼盘提交
        void submitBuilding(GetIdentityInfoBean data, int createCompany, int identityType,
                            String buildingName, String address,
                            int district, int business, String mPath);
        //创建联办提交
        void submitJointWork(GetIdentityInfoBean data, int createCompany, int identityType,
                             String branchesName, String address,
                             int district, int business, String mPath);
    }
}
