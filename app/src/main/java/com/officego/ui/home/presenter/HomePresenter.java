package com.officego.ui.home.presenter;

import android.content.Context;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.retrofit.RpcErrorCode;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.contract.HomeContract;
import com.officego.ui.home.model.BannerBean;
import com.officego.ui.home.model.BuildingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    //banner list
    private List<String> bannerList = new ArrayList<>();

    public HomePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getBannerList() {
        OfficegoApi.getInstance().getBannerList(new RetrofitCallback<List<BannerBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<BannerBean.DataBean> data) {
                if (isViewAttached()) {
                    bannerList.clear();
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            bannerList.add(data.get(i).getImg());
                        }
                        mView.bannerListSuccess(bannerList, data);
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, List<BannerBean.DataBean> data) {
                if (isViewAttached()) {
                }
            }
        });
    }

    @Override
    public void getBuildingList(Context context,int pageNo, String btype, String district, String business, String line,
                                String nearbySubway, String area, String dayPrice, String seats, String decoration,
                                String houseTags, String sort, String keyWord) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getBuildingList(context,pageNo, btype, district, business,
                line, nearbySubway, area, dayPrice, seats, decoration,
                houseTags, sort, keyWord,  Constants.LONGITUDE, Constants.LATITUDE,new RetrofitCallback<BuildingBean>() {
                    @Override
                    public void onSuccess(int code, String msg, BuildingBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.BuildingListSuccess(data.getList(), data.getList().size() >= 10);
                            mView.endRefresh();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, BuildingBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.endRefresh();
                            if (code == Constants.ERROR_CODE_5002 || code == RpcErrorCode.RPC_ERR_TIMEOUT) {
                                mView.BuildingListFail(code, msg);
                            }
                        }
                    }
                });
    }
}
