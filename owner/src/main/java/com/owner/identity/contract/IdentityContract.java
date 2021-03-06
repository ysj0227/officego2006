package com.owner.identity.contract;

import android.content.Context;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.IdentityRejectBean;
import com.officego.commonlib.common.model.SearchListBean;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface IdentityContract {
    interface View extends BaseView {

        void searchBuildingSuccess(List<SearchListBean.DataBean> data);

        void identityMessageSuccess(IdentityRejectBean data);

//        void submitIdentitySuccess();

        void uploadSuccess(int imageType, UploadImageBean data);

        void userInfoSuccess(UserMessageBean data);

        void updateUserSuccess();
    }

    interface Presenter {

        void getUserInfo(Context context);

        void searchList(String keywords);

        void getIdentityMessage(int buildingId);

        void submitIdentityMessage(int btype, int isFrist, String buildingName, String mainPic,
                                   String premisesPermit, String businessLicense, String materials, String idFront,
                                   String idBack, int isHolder, String buildId, int buildingId,
                                   int districtId, int businessDistrict, String address);

        void uploadImage(int imageType, List<String> mFilePath);

        void updateUserInfo(String avatar, String nickname, String sex, String job, String wx);
    }
}
