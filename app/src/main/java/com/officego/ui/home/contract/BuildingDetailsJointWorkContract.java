package com.officego.ui.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.ui.home.model.BuildingDetailsBean;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.ui.home.model.BuildingJointWorkBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions: 楼盘，网点详情公用
 **/
public interface BuildingDetailsJointWorkContract {

    interface View extends BaseView {

        void BuildingDetailsSuccess(BuildingDetailsBean data);

        void BuildingJointWorkDetailsSuccess(BuildingJointWorkBean data);

        void favoriteSuccess();

        void favoriteFail();

        void buildingSelectListSuccess(int total,List<BuildingDetailsChildBean.ListBean> list);
    }

    interface Presenter {

        void getBuildingDetails(String btype, String buildingId, String area, String dayPrice,
                                String decoration, String houseTags, String seats);

        void favorite(String buildingId, int flag);

        void getBuildingSelectList(int pageNo, String btype, String buildingId, String area, String dayPrice,
                                   String decoration, String houseTags, String seats);
    }
}
