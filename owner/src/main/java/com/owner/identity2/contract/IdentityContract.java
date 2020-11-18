package com.owner.identity2.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.identity.model.IdentityBuildingBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface IdentityContract {
    interface View extends BaseView {

        void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data);
    }

    interface Presenter {

        void searchBuilding(String keywords);
    }
}
