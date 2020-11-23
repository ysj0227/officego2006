package com.owner.identity2.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.identity2.contract.CreateBuildingContract;

/**
 * Created by shijie
 * Date 2020/11/3
 **/
public class CreateBuildingPresenter extends BasePresenter<CreateBuildingContract.View>
        implements CreateBuildingContract.Presenter {


    @Override
    public void uploadImage(String mFilePath) {
        //1楼图片2视频3房源图片4认证文件夹
        //封面图上传图片
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().uploadSingleImageUrl(Constants.TYPE_IMAGE_BUILDING,
                mFilePath, new RetrofitCallback<UploadImageBean>() {
            @Override
            public void onSuccess(int code, String msg, UploadImageBean data) {
                if (isViewAttached()) {
                    mView.uploadSuccess(data);
                    mView.hideLoadingDialog();
                }
            }

            @Override
            public void onFail(int code, String msg, UploadImageBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE ) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }
}
