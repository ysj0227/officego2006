package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.owner.UploadImageBean;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface CreateBuildingContract {
    interface View extends BaseView {

//        void buildingInfoSuccess();

        void uploadSuccess(UploadImageBean data);
    }

    interface Presenter {

//        void getBuildingInfo();

        void uploadImage(String mFilePath);
    }
}
