package com.owner.home.contract;

import com.officego.commonlib.base.BaseView;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.HouseEditBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public interface HouseContract {
    interface View extends BaseView {
        //编辑
        void houseEditSuccess(HouseEditBean data);

        //房源特色
        void houseUniqueSuccess(List<DirectoryBean.DataBean> data);

        //装修类型
        void decoratedTypeSuccess(List<DirectoryBean.DataBean> data);

    }

    interface Presenter {

        void getHouseUnique();

        void getDecoratedType();

        void getHouseEdit(int houseId, int isTemp);
    }
}
