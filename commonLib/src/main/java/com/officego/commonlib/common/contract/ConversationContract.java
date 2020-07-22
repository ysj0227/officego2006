package com.officego.commonlib.common.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.IdentitychattedMsgBean;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface ConversationContract {

    interface View extends BaseView {
        void houseSuccess(ChatHouseBean data);

        void firstChatSuccess(FirstChatBean data);

        void identityChattedMsgSuccess(IdentitychattedMsgBean data);
    }

    interface Presenter {
        void getHouseDetails(int buildingId,int houseId, String targetId);

        void isFirstChat(int buildingId,int houseId,  String targetId);

        void identityChattedMsg(String targetId);
    }
}
