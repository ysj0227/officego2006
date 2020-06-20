package com.officego.ui.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.ui.home.model.BuildingBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface HomeContract {

    interface View extends BaseView {

        void endRefresh();

        void bannerListSuccess(List<String> bannerList);

        void BuildingListSuccess(List<BuildingBean.ListBean> list, boolean hasMore);

        void BuildingListFail(int code,String msg);
    }

    interface Presenter {

        void getBannerList();

        void getBuildingList(int pageNo, String btype, String district, String business, String line,
                             String nearbySubway, String area, String dayPrice, String seats, String decoration,
                             String houseTags, String sort, String keyWord);
    }
}
