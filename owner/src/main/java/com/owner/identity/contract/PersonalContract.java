package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface PersonalContract {
    interface View extends BaseView {

        void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data);

    }

    interface Presenter {
        void getBuilding(String keyword);
    }
}
