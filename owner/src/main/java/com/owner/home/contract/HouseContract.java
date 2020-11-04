package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface HouseContract {
    interface View extends BaseView {
        //编辑
        void houseEditSuccess(HouseEditBean data);

        //房源特色
        void houseUniqueSuccess(List<DirectoryBean.DataBean> data);

        //装修类型
        void decoratedTypeSuccess(List<DirectoryBean.DataBean> data);

        //是否户型介绍上传  还是多图
        void uploadSuccess(boolean isIntroduceLayout, UploadImageBean data);

        void editSaveSuccess();

    }

    interface Presenter {

        void getHouseUnique();

        void getDecoratedType();

        void getHouseEdit(int houseId, int isTemp);

        void uploadImage(List<ImageBean> mFilePath);

        void uploadSingleImage(String mFilePath);

        void saveEdit(int id, int isTemp, String title, String area,
                      String seats, String dayPrice, String monthPrice,
                      String floor, String clearHeight, String storeyHeight,
                      String minimumLease, String rentFreePeriod,
                      String propertyHouseCosts, String decoration, String unitPatternRemark,
                      String tags, String unitPatternImg,
                      String mainPic, String addImgUrl, String delImgUrl);
    }
}
