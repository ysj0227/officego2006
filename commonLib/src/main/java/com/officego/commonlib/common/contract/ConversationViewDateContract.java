package com.officego.commonlib.common.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.owner.ChatBuildingBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface ConversationViewDateContract {

    interface View extends BaseView {
        void houseSuccess(ChatHouseBean data);

        void buildingListSuccess( List<ChatBuildingBean.DataBean> data);

    }

    interface Presenter {
        void getHouseDetails(int buildingId, int houseId, String targetId);

        void getOwnerHouseList(String targetId);
    }
}
