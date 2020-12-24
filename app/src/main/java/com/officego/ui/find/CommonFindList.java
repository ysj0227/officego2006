package com.officego.ui.find;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/24
 **/
class CommonFindList {
    //团队规模
    public static List<WantFindBean> peopleNumList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean = new WantFindBean("1-5人", "1,5");
        list.add(bean);
        WantFindBean bean1 = new WantFindBean("6-10人", "6,10");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("11-20人", "11,20");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("21-50人", "21,50");
        list.add(bean3);
        WantFindBean bean4 = new WantFindBean("51-100人", "51,100");
        list.add(bean4);
        WantFindBean bean5 = new WantFindBean("100人以上", "100,99999999");
        list.add(bean5);
        return list;
    }

    //租期 -想租多久
    public static List<WantFindBean> rentTimeList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean = new WantFindBean("0-6个月", "0,6");
        list.add(bean);
        WantFindBean bean1 = new WantFindBean("6-12个月", "6,12");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("1-3年", "12,36");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("我都可以", "0");
        list.add(bean3);
        return list;
    }

    //因素
    public static List<WantFindBean> factorList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean1 = new WantFindBean("1", "交通便利");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("2", "拎包入住");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("3", "折扣特价");
        list.add(bean3);
        WantFindBean bean4 = new WantFindBean("4", "核心商圈");
        list.add(bean4);
        WantFindBean bean5 = new WantFindBean("5", "优惠政策");
        list.add(bean5);
        WantFindBean bean6 = new WantFindBean("6", "金牌物业");
        list.add(bean6);
        return list;
    }

}
