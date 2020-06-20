package com.officego.ui.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.ui.home.model.ChatsBean;
import com.officego.ui.home.model.HouseOfficeDetailsBean;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface BuildingDetailsChildContract {

    interface View extends BaseView {

        void detailsSuccess(HouseOfficeDetailsBean data);

        void favoriteChildSuccess();

        void chatSuccess(ChatsBean data);

        void chatFail();
    }

    interface Presenter {
        void getDetails(String btype, String houseId);

        void favoriteChild(String houseId, int flag);
        //找房东去聊天
        void gotoChat(String houseId);
    }
}
