package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.DirectoryBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface BuildingContract {
    interface View extends BaseView {
        //房源特色
        void houseUniqueSuccess(List<DirectoryBean.DataBean> data);

    }

    interface Presenter {

        void getHouseUnique();

    }
}
