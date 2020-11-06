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
public interface BuildingContract {
    interface View extends BaseView {
        //房源特色
        void houseUniqueSuccess(List<DirectoryBean.DataBean> data);

        //编辑
        void buildingEditSuccess(BuildingEditBean data);

        void uploadSuccess(UploadImageBean data);

        void editSaveSuccess();

    }

    interface Presenter {

        void getBuildingUnique();

        void getBuildingEdit(int buildingId, int isTemp);

        void uploadImage(int type,List<ImageBean> mFilePath);

        void saveEdit(int buildingId, int isTemp, int buildingType, String buildingNum,
                      int districtId, int area, String address, String totalFloor,
                      String completionTime, String refurbishedTime, String constructionArea,
                      String clearHeight, String storeyHeight, String property, String propertyCosts,
                      String parkingSpace, String ParkingSpaceRent, String airConditioning, String airConditioningFee,
                      String passengerLift, String cargoLift, String buildingIntroduction,
                      String internet, String settlementLicence,String tags ,String mainPic, String addImgUrl, String delImgUrl);
    }
}
