package com.officego.ui.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.ui.home.model.BannerBean;
import com.officego.ui.home.model.BrandRecommendBean;
import com.officego.ui.home.model.HomeHotBean;
import com.officego.ui.home.model.TodayReadBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface HomeContract {

    interface View extends BaseView {

        void bannerListSuccess(List<String> bannerList, List<BannerBean.DataBean> data);

        void endRefresh();

        void todayReadSuccess(boolean isShowView, List<TodayReadBean.DataBean> dataList);

        void todayReadFail();

        void brandSuccess(boolean isShowView, List<BrandRecommendBean.DataBean> dataList);

        void brandFail();

        void hotListSuccess(HomeHotBean data);
    }

    interface Presenter {

        void getBannerList();

        void getTodayRead();

        void getBrandManagement();

        void getHotList();
    }
}
