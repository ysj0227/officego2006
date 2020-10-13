package com.officego.ui.message.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.IdentitychattedMsgBean;
import com.officego.commonlib.common.model.RongUserInfoBean;
import com.officego.commonlib.common.model.ExchangeContactsBean;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface ConversationContract {

    interface View extends BaseView {
        void houseSuccess(ChatHouseBean data);

        void exchangeContactsSuccess(boolean isCanExchange);

        void exchangeContactsFail(String msg);

        void firstChatSuccess(FirstChatBean data);

        void identityChattedMsgSuccess(IdentitychattedMsgBean data);

        void rongTargetInfoSuccess(RongUserInfoBean data);
    }

    interface Presenter {
        void firstChatApp(String targetId,int buildingId, int houseId, String getHouseChatId);

        void exchangeContactsVerification(String targetId);

        void isFirstChat(int buildingId, int houseId, String targetId);

        void identityChattedMsg(String targetId);

        void getRongTargetInfo(String targetId);
    }
}
