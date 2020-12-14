package com.owner.identity.presenter;

import android.app.Activity;
import android.content.Context;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.IdentityRejectBean;
import com.officego.commonlib.common.model.SearchListBean;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.identity.contract.IdentityContract;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/3
 **/
public class IdentityPresenter extends BasePresenter<IdentityContract.View>
        implements IdentityContract.Presenter {

    private Activity context;
    private int flag;

    public IdentityPresenter(Activity context, int flag) {
        this.context = context;
        this.flag = flag;
    }

    @Override
    public void getUserInfo(Context context) {
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getUserMsg(new RetrofitCallback<UserMessageBean>() {
            @Override
            public void onSuccess(int code, String msg, UserMessageBean data) {
                if (isViewAttached()) {
                    mView.userInfoSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, UserMessageBean data) {
                if (isViewAttached()) {

                }
            }
        });
    }

    @Override
    public void searchList(String keyword) {
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().searchList2(keyword,
                new RetrofitCallback<List<SearchListBean.DataBean>>() {
                    @Override
                    public void onSuccess(int code, String msg, List<SearchListBean.DataBean> data) {
                        if (isViewAttached()) {
                            mView.searchBuildingSuccess(data);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, List<SearchListBean.DataBean> data) {
                        if (isViewAttached()) {
                            if (code == Constants.DEFAULT_ERROR_CODE) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }

    @Override
    public void getIdentityMessage(int buildingId) {
        mView.showLoadingDialog();
        com.owner.rpc.OfficegoApi.getInstance().getIdentityMessage(buildingId, new RetrofitCallback<IdentityRejectBean>() {
            @Override
            public void onSuccess(int code, String msg, IdentityRejectBean data) {
                if (isViewAttached()) {
                    mView.identityMessageSuccess(data);
                    mView.hideLoadingDialog();
                }
            }

            @Override
            public void onFail(int code, String msg, IdentityRejectBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    @Override
    public void submitIdentityMessage(int btype, int isFrist, String buildingName,
                                      String mainPic, String premisesPermit, String businessLicense,
                                      String materials, String idFront, String idBack, int isHolder,
                                      String buildId, int buildingId, int districtId, int businessDistrict,
                                      String address) {
        mView.showLoadingDialog();
        com.owner.rpc.OfficegoApi.getInstance().submitIdentity(btype, isFrist, buildingName,
                mainPic, premisesPermit, businessLicense,
                materials, idFront, idBack, isHolder,
                buildId, buildingId, districtId, businessDistrict,
                address, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            submitIdentitySuccessDialog();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }

    @Override
    public void uploadImage(int imageType, List<String> mFilePath) {
        //1楼图片2视频3房源图片4认证文件夹
        //认证上传图片
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().uploadImage(Constants.TYPE_IMAGE_IDENTITY,
                mFilePath, new RetrofitCallback<UploadImageBean>() {
                    @Override
                    public void onSuccess(int code, String msg, UploadImageBean data) {
                        if (isViewAttached()) {
                            mView.uploadSuccess(imageType, data);
                            mView.hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, UploadImageBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE || code == Constants.ERROR_CODE_6028) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }

    @Override
    public void updateUserInfo(String avatar, String nickname, String sex, String job, String wx) {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().updateUserInfoCard(avatar, nickname, sex,
                job, wx, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.updateUserSuccess();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            if (code == Constants.DEFAULT_ERROR_CODE) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }

    //认证提交成功
    private void submitIdentitySuccessDialog() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle("提交成功")
                .setMessage("我们会在1-2个工作日完成审核")
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    context.finish();
                    //通知提交认证
                    if (Constants.IDENTITY_FIRST == flag) {
                        BaseNotification.newInstance().postNotificationName(
                                CommonNotifications.checkedIdentitySuccess, "checkedIdentitySuccess");
                    } else if (Constants.IDENTITY_REJECT == flag) {
                        BaseNotification.newInstance().postNotificationName(
                                CommonNotifications.rejectBuildingSuccess, "rejectBuildingSuccess");
                    } else {
                        //添加楼盘网点成功
                        BaseNotification.newInstance().postNotificationName(
                                CommonNotifications.updateBuildingSuccess, "updateBuildingSuccess");
                    }
                }).create();
        dialog.showWithOutTouchable(false);
        dialog.setCancelable(false);
    }

}
