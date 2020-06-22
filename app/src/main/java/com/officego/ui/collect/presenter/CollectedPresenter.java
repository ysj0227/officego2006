package com.officego.ui.collect.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.collect.contract.CollectedContract;
import com.officego.ui.collect.model.CollectBuildingBean;
import com.officego.ui.collect.model.CollectHouseBean;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class CollectedPresenter extends BasePresenter<CollectedContract.View>
        implements CollectedContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void favoriteBuildingList(int pageNo, String longitude, String latitude) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().favoriteBuildingList(pageNo, longitude, latitude,
                new RetrofitCallback<CollectBuildingBean>() {
                    @Override
                    public void onSuccess(int code, String msg, CollectBuildingBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.endRefresh();
                            mView.favoriteBuildingListSuccess(data);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, CollectBuildingBean data) {
                        LogCat.e(TAG, "favoriteBuildingList onFail code=" + code + "  msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.endRefresh();
                            if (code == Constants.ERROR_CODE_5002) {
                                mView.favoriteBuildingListFail(code, msg);
                            }
                        }
                    }
                });
    }

    @Override
    public void favoriteHouseList(int pageNo, String longitude, String latitude) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().favoriteHouseList(pageNo, longitude, latitude,
                new RetrofitCallback<CollectHouseBean>() {
                    @Override
                    public void onSuccess(int code, String msg, CollectHouseBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.endRefresh();
                            mView.favoriteHouseListSuccess(data);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, CollectHouseBean data) {
                        LogCat.e(TAG, "favoriteHouseList onFail code=" + code + "  msg=" + msg);
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.endRefresh();
                            if (code == Constants.ERROR_CODE_5002) {
                                mView.favoriteHouseListFail(code, msg);
                            }
                        }
                    }
                });
    }
}