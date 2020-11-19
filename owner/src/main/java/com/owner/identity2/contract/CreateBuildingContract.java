package com.owner.identity2.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.owner.identity.model.IdentityBuildingBean;

import java.util.List;

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
