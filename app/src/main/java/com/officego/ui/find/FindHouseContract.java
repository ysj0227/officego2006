package com.officego.ui.find;

import com.officego.commonlib.base.BaseView;
import com.officego.ui.find.model.DirectoryBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public interface FindHouseContract {

    interface View extends BaseView {
        //房源特色
        void getHouseUniqueSuccess(List<DirectoryBean.DataBean> data);

        void getHouseUniqueFail(int code, String msg);

        //装修类型
        void getDecoratedTypeSuccess(List<DirectoryBean.DataBean> data);

        void getDecoratedTypeFail(int code, String msg);

        //我想找
//        void wantToFindSuccess();
    }

    interface Presenter {

        void getHouseUnique();

        void getDecoratedType();

        void wantToFind(String btype, String constructionArea, String rentPrice,
                        String simple, String decoration, String tags);
    }
}
