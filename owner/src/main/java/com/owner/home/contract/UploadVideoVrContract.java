package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface UploadVideoVrContract {
    interface View extends BaseView {
        void publishSuccess();
    }

    interface Presenter {

        void publishBuilding(int flay,int buildingId, int isTemp, String vr);
    }
}
