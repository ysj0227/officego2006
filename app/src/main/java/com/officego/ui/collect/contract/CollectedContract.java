package com.officego.ui.collect.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.ui.collect.model.CollectBuildingBean;
import com.officego.ui.collect.model.CollectHouseBean;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public interface CollectedContract {

    interface View extends BaseView {
        void endRefresh();

        void favoriteBuildingListSuccess(CollectBuildingBean data);

        void favoriteBuildingListFail(int code, String msg);

        void favoriteHouseListSuccess(CollectHouseBean data);

        void favoriteHouseListFail(int code, String msg);
    }

    interface Presenter {

        //楼盘列表
        void favoriteBuildingList(int pageNo, String longitude, String
                latitude);

        //房源列表
        void favoriteHouseList(int pageNo, String longitude, String
                latitude);

    }
}
