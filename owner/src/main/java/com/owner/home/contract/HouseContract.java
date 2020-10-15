package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.DirectoryBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface HouseContract {
    interface View extends BaseView {
        //基础服务
        void baseServices(List<DirectoryBean.DataBean> data);

        //企业服务
        void companyServices(List<DirectoryBean.DataBean> data);

        //房源特色
        void houseUniqueSuccess(List<DirectoryBean.DataBean> data);

        //装修类型
        void decoratedTypeSuccess(List<DirectoryBean.DataBean> data);

    }

    interface Presenter {
        void getBaseServices();

        void getCompanyServices();

        void getHouseUnique();

        void getDecoratedType();

    }
}
