package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.BuildingEditBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface BuildingContract {
    interface View extends BaseView {
        //房源特色
        void houseUniqueSuccess(List<DirectoryBean.DataBean> data);

        //编辑
        void buildingEditSuccess(BuildingEditBean data);

    }

    interface Presenter {

        void getBuildingUnique();

        void getBuildingEdit(int buildingId, int isTemp);
    }
}
