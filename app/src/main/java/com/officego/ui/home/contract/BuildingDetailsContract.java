package com.officego.ui.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.NearbyBuildingBean;
import com.officego.ui.home.model.BuildingDetailsBean;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.ui.home.model.BuildingJointWorkBean;
import com.officego.ui.home.model.ChatsBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions: 楼盘，网点详情公用
 **/
public interface BuildingDetailsContract {

    interface View extends BaseView {

        void BuildingDetailsSuccess(BuildingDetailsBean data);

        void BuildingTakeOff(String msg);//下架

        void favoriteSuccess();

        void favoriteFail();

        void buildingSelectListSuccess(int totals, List<BuildingDetailsChildBean.ListBean> list);

        void chatSuccess(ChatsBean data);

        void nearbyBuildingSuccess(List<NearbyBuildingBean.DataBean> data);

    }

    interface Presenter {

        void getBuildingDetails(String btype, String buildingId, String area, String dayPrice,
                                String decoration, String houseTags, String seats);

        void getBuildingDetailsOwner(String btype, String buildingId, int isTemp);

        void favorite(String buildingId, int flag);

        void getBuildingSelectList(int pageNo, String btype, String buildingId, String area, String dayPrice,
                                   String decoration, String houseTags, String seats);

        void gotoChat(String buildingId);

        void getNearbyBuildingList(int buildingId);
    }
}
