package com.officego.ui.chatlist.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.ui.find.model.DirectoryBean;
import com.officego.ui.home.model.QueryHistoryKeywordsBean;
import com.officego.ui.home.model.SearchListBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface ChatListContract {

    interface View extends BaseView {

        void chatListSuccess(List<ChatListBean.ListBean> data);

    }

    interface Presenter {
        void getChatList();
    }
}
