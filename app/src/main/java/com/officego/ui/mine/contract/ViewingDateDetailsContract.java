package com.officego.ui.mine.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.ui.mine.model.ViewingDateBean;
import com.officego.ui.mine.model.ViewingDateDetailsBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface ViewingDateDetailsContract {
    interface View extends BaseView {

        void dateSuccess(ViewingDateDetailsBean data);

        void dateFail(int code, String msg);
    }

    interface Presenter {

        void getViewingDateDetails(int scheduleId);
    }
}
