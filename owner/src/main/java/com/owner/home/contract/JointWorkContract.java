package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.BuildingEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface JointWorkContract {
    interface View extends BaseView {
        void buildingEditSuccess(BuildingEditBean data);

        void houseUniqueSuccess(List<DirectoryBean.DataBean> data);

        void roomMatchingSuccess(List<DirectoryBean.DataBean> data);

        void baseServiceSuccess(List<DirectoryBean.DataBean> data);

        void companyServiceSuccess(List<DirectoryBean.DataBean> data);

        void uploadSuccess(UploadImageBean data);
    }

    interface Presenter {
        void getBuildingEdit(int buildingId, int isTemp);

        void getBranchUnique();

        void getRoomMatching();

        void getBaseService();

        void getCompanyService();

        void uploadImage(List<ImageBean> mFilePath);
    }
}
