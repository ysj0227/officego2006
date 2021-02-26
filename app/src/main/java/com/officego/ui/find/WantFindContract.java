package com.officego.ui.find;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.DirectoryBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2021/1/7
 **/
public interface WantFindContract {

    interface View extends BaseView {

        void factorSuccess(List<DirectoryBean.DataBean> list);

        void saveSuccess();
    }

    interface Presenter {

        void getFactorList();

        void save(String person, String rent, String factor);

        void customisedHouseSave(String person, String rent, String factor);
    }
}
