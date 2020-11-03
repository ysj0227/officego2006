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
public interface AddContract {
    interface View extends BaseView {

        //是否户型介绍上传  还是多图
        void uploadSuccess(boolean isIntroduceLayout, UploadImageBean data);
    }

    interface Presenter {

        void uploadImage(List<ImageBean> mFilePath);

        void uploadSingleImage(String mFilePath);
    }
}
