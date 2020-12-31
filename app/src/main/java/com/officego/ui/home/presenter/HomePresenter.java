package com.officego.ui.home.presenter;

import android.content.Context;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.contract.HomeContract;
import com.officego.ui.home.model.BannerBean;
import com.officego.ui.home.model.BrandRecommendBean;
import com.officego.ui.home.model.HomeHotBean;
import com.officego.ui.home.model.TodayReadBean;

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

    //今日看点
    @Override
    public void getTodayRead() {
        OfficegoApi.getInstance().todayRead(new RetrofitCallback<List<TodayReadBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<TodayReadBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.todayReadSuccess(data != null && data.size() > 0, data);
                    mView.endRefresh();
                }
            }

            @Override
            public void onFail(int code, String msg, List<TodayReadBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.todayReadFail();
                    mView.endRefresh();
                }
            }
        });
    }
    //品牌入驻
    @Override
    public void getBrandManagement() {
        OfficegoApi.getInstance().brandManagement(new RetrofitCallback<List<BrandRecommendBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<BrandRecommendBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.brandSuccess(data != null && data.size() > 0, data);
                    mView.endRefresh();
                }
            }

            @Override
            public void onFail(int code, String msg, List<BrandRecommendBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.brandFail();
                    mView.endRefresh();
                }
            }
        });
    }

    @Override
    public void getHotList() {
        OfficegoApi.getInstance().getHotsList(new RetrofitCallback<HomeHotBean>() {
            @Override
            public void onSuccess(int code, String msg, HomeHotBean data) {
                if (isViewAttached()) {
                   mView.hotListSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, HomeHotBean data) {
                if (isViewAttached()) {
                    mView.endRefresh();
                }
            }
        });
    }

}
