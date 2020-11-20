package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.owner.BuildingJointWorkBean;
import com.officego.commonlib.common.model.owner.HouseBean;
import com.owner.mine.model.UserOwnerBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface HomeContract {
    interface View extends BaseView {
        void endRefresh();

        void buildingJointWorkListSuccess(BuildingJointWorkBean data);

        void houseListSuccess(List<HouseBean.ListBean> data, boolean hasMore);

        void userInfoSuccess(UserOwnerBean data);

        void initHouseList(int buildingIdPosition,BuildingJointWorkBean data);

        void houseDeleteSuccess();

        void publishOrOffHouseSuccess(int currentStatus);
    }

    interface Presenter {

        void getBuildingJointWorkList();    //获取楼盘或网点列表

        void getHouseList(int buildingId, int isTemp, int pageNo);

        void getUserInfo();

        void getHouseList(int buildingIdPosition);//第几个楼盘网点

        void isPublishHouse(int houseId, int isRelease,int isTemp);//上下架发布

        void houseDelete(int houseId, int isTemp);//删除房源
    }
}
