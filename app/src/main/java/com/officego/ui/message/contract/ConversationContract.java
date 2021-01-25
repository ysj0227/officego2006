package com.officego.ui.message.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.RongUserInfoBean;

import io.rong.imlib.model.Message;

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

        void rongTargetInfoSuccess(RongUserInfoBean data);
    }

    interface Presenter {
        void firstChatApp(String targetId,int buildingId, int houseId, String getHouseChatId);

        void exchangeContactsVerification(String targetId);

        void isFirstChat(int buildingId, int houseId, String targetId);

        void getRongTargetInfo(String targetId);

        void recordChatTime(String targetId, int houseId, int buildingId, Message msg);
    }
}
