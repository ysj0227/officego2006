package com.officego.ui.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.ui.home.model.QueryHistoryKeywordsBean;
import com.officego.ui.home.model.SearchListBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface SearchContract {

    interface View extends BaseView {

        void historySuccess(List<QueryHistoryKeywordsBean.DataBean> data);

        void hotSuccess(List<DirectoryBean.DataBean> data);

        void searchListSuccess(List<SearchListBean.DataBean> list);

        void clearHistorySuccess();
    }

    interface Presenter {
        void getHistory();

        void getHot();

        void searchList(String keywords);

        void addSearchKeywords(String keywords);

        void clearSearchKeywords();
    }
}
