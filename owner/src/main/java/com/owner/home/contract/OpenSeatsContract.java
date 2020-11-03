package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.owner.HouseEditBean;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface OpenSeatsContract {
    interface View extends BaseView {
        void houseEditSuccess(HouseEditBean data);
    }

    interface Presenter {
        void getHouseEdit(int houseId, int isTemp);
    }
}
