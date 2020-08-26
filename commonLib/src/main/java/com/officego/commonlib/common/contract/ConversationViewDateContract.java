package com.officego.commonlib.common.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.ChatHouseBean;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface ConversationViewDateContract {

    interface View extends BaseView {
        void houseSuccess(ChatHouseBean data);

    }

    interface Presenter {
        void getHouseDetails(int buildingId, int houseId, String targetId);
    }
}
