package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface OpenSeatsContract {
    interface View extends BaseView {
        void houseEditSuccess(HouseEditBean data);

        void uploadSuccess(UploadImageBean data);
    }

    interface Presenter {
        void getHouseEdit(int houseId, int isTemp);

        void uploadImage(List<ImageBean> mFilePath);
    }
}
