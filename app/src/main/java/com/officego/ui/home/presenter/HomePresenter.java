package com.officego.ui.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.contract.HomeContract;
import com.officego.ui.home.model.BannerBean;
import com.officego.ui.home.model.BrandRecommendBean;
import com.officego.ui.home.model.HomeHotBean;
import com.officego.ui.home.model.HomeMeetingBean;
import com.officego.ui.home.model.TodayReadBean;
import com.officego.utils.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private final SuperSwipeRefreshLayout mSwipeRefreshLayout;
    private final List<String> bannerList = new ArrayList<>();

    public HomePresenter(SuperSwipeRefreshLayout mSwipeRefreshLayout) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
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
                    mView.endRefresh();
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

    //品牌入驻 type 1首页2筛选
    @Override
    public void getBrandManagement() {
        OfficegoApi.getInstance().brandManagement(1, new RetrofitCallback<List<BrandRecommendBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<BrandRecommendBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.brandSuccess(data != null && data.size() > 0, data);
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

    //先获取会议室再获取热门推荐
    @Override
    public void getHotList() {
        if (mSwipeRefreshLayout == null || !mSwipeRefreshLayout.isRefreshing()) {
            mView.showLoadingDialog();
        }
        OfficegoApi.getInstance().getHomeMeeting(new RetrofitCallback<HomeMeetingBean.DataBean>() {
            @Override
            public void onSuccess(int code, String msg, HomeMeetingBean.DataBean data) {
                if (isViewAttached()) {
                    getHot(data);
                }
            }

            @Override
            public void onFail(int code, String msg, HomeMeetingBean.DataBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.endRefresh();
                }
            }
        });

    }

    public void getHot(HomeMeetingBean.DataBean meetData) {
        OfficegoApi.getInstance().getHotsList(new RetrofitCallback<HomeHotBean.DataBean>() {
            @Override
            public void onSuccess(int code, String msg, HomeHotBean.DataBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.hotListSuccess(meetData, data);
                    mView.endRefresh();
                }
            }

            @Override
            public void onFail(int code, String msg, HomeHotBean.DataBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.endRefresh();
                }
            }
        });
    }

}
