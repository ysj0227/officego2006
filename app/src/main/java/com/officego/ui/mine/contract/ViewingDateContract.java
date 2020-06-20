package com.officego.ui.mine.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.ui.mine.model.UserBean;
import com.officego.ui.mine.model.ViewingDateBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface ViewingDateContract {
    interface View extends BaseView {

        void viewingDateSuccess(List<ViewingDateBean.DataBean> data);

        void viewingDateFail(int code, String msg);
    }

    interface Presenter {

        void getViewingDate(long startTime,long endTime);

        void getOldViewingDate(long startTime,long endTime);
    }
}
