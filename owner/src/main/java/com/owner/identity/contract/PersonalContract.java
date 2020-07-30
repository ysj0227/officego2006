package com.owner.identity.contract;

import com.officego.commonlib.base.BaseView;
import com.owner.identity.model.GetIdentityInfoBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public interface PersonalContract {
    interface View extends BaseView {

        void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data);

        void checkBuildingInfoSuccess();

        void getIdentityInfoSuccess(GetIdentityInfoBean data,boolean isFirstGetInfo);

        void submitSuccess();

        void submitTimeout();

        void deleteImageSuccess(boolean isPremisesImage,int position);
    }

    interface Presenter {
        void getBuilding(String keyword);

        void checkBuilding(int identityType,String name);

        void getIdentityInfo(int identityType,boolean isFirstGetInfo);

        void submit(GetIdentityInfoBean data, int createCompany, int identityType, int leaseType,
                    boolean isSelectedBuilding, String buildingId, String buildingName, String buildingAddress,
                    String userName, String idCard, String isCardFrontPath, String isCardBackPath,
                    List<ImageBean> mFilePremisesPath, List<ImageBean> mFileContractPath);

        void deleteImage(boolean isPremisesImage, int id, int position);
    }
}
