package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface UploadVideoVrContract {
    interface View extends BaseView {
        void publishSuccess();
    }

    interface Presenter {

        void publishBuilding(int buildingId, int isTemp, String vr);
    }
}
