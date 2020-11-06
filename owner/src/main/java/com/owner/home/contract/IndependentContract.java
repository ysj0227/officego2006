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
public interface IndependentContract {
    interface View extends BaseView {

        void houseEditSuccess(HouseEditBean data);

        //是否户型介绍上传  还是多图
        void uploadSuccess(boolean isIntroduceLayout, UploadImageBean data);

        void editSaveSuccess();

        void addHouseSuccess(String id);
    }

    interface Presenter {
        void getHouseEdit(int houseId, int isTemp);

        void uploadImage(int type,List<ImageBean> mFilePath);

        void uploadSingleImage(int type,String mFilePath);

        void saveEdit(int id, int isTemp, String title,
                      String seats, String area, String monthPrice,
                      String floor, String minimumLease, String rentFreePeriod,
                      String conditioningType, String conditioningTypeCost,
                      String clearHeight, String unitPatternRemark,
                      String unitPatternImg, String mainPic, String addImgUrl, String delImgUrl);

        void addHouse(int buildingId, int isTemp,String title,
                      String seats, String area, String monthPrice,
                      String floor, String minimumLease, String rentFreePeriod,
                      String conditioningType, String conditioningTypeCost,
                      String clearHeight, String unitPatternRemark,
                      String unitPatternImg, String mainPic, String addImgUrl, String delImgUrl);
    }
}
