package com.owner.identity2.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface IdentityContract {
    interface View extends BaseView {

        void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data);

        void uploadSuccess(int imageType, UploadImageBean data);
    }

    interface Presenter {

        void searchBuilding(String keywords);

        void uploadImage(int imageType, List<String> mFilePath);
    }
}
