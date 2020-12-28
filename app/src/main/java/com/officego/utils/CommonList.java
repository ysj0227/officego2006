package com.officego.utils;

import com.officego.ui.find.WantFindBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/24
 **/
public class CommonList {
    //我想找-团队规模
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
        WantFindBean bean5 = new WantFindBean("100人以上", "101,99999999");
        list.add(bean5);
        return list;
    }

    //我想找-租期 -想租多久
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

    //我想找-因素
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

    //筛选面积
    public static List<WantFindBean> areaList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean1 = new WantFindBean("0,99999999", "不限");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("0,20", "20㎡以下");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("20,50", "20-50㎡");
        list.add(bean3);
        WantFindBean bean4 = new WantFindBean("50,70", "50-70㎡");
        list.add(bean4);
        WantFindBean bean5 = new WantFindBean("70,100", "70-100㎡");
        list.add(bean5);
        WantFindBean bean6 = new WantFindBean("100,99999999", "100㎡以上");
        list.add(bean6);
        return list;
    }

    //筛选租金
    public static List<WantFindBean> rentList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean1 = new WantFindBean("0,99999999", "不限");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("0,2", "2以下");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("2,5", "2-5");
        list.add(bean3);
        WantFindBean bean4 = new WantFindBean("5,10", "5-10");
        list.add(bean4);
        WantFindBean bean5 = new WantFindBean("10,15", "10-15");
        list.add(bean5);
        WantFindBean bean6 = new WantFindBean("15,99999999", "15以上");
        list.add(bean6);
        return list;
    }

    //筛选工位
    public static List<WantFindBean> seatsList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean1 = new WantFindBean("0,99999999", "不限");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("0,4", "5个以下");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("5,10", "5-10个");
        list.add(bean3);
        WantFindBean bean4 = new WantFindBean("10,20", "10-20个");
        list.add(bean4);
        WantFindBean bean5 = new WantFindBean("20,30", "20-30个");
        list.add(bean5);
        WantFindBean bean6 = new WantFindBean("30,99999999", "30个以上");
        list.add(bean6);
        return list;
    }

}
