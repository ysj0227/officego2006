package com.officego.utils;

import com.officego.ui.find.WantFindBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/24
 **/
public class CommonList {
    public static final String SEARCH_MAX = "99999999";

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
        WantFindBean bean5 = new WantFindBean("100人以上", "101," + SEARCH_MAX);
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

    //筛选-写字楼面积
    public static List<WantFindBean> areaList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean1 = new WantFindBean("0," + SEARCH_MAX, "不限");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("0,100", "0-100㎡");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("100,200", "100-200㎡");
        list.add(bean3);
        WantFindBean bean4 = new WantFindBean("200,300", "200-300㎡");
        list.add(bean4);
        WantFindBean bean5 = new WantFindBean("300,500", "300-500㎡");
        list.add(bean5);
        WantFindBean bean6 = new WantFindBean("500," + SEARCH_MAX, "500㎡以上");
        list.add(bean6);
        return list;
    }

    //筛选写字楼租金
    public static List<WantFindBean> rentList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean1 = new WantFindBean("0," + SEARCH_MAX, "不限");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("0,3", "0-3");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("3,5", "3-5");
        list.add(bean3);
        WantFindBean bean4 = new WantFindBean("5,8", "5-8");
        list.add(bean4);
        WantFindBean bean5 = new WantFindBean("8,12", "8-12");
        list.add(bean5);
        WantFindBean bean6 = new WantFindBean("12," + SEARCH_MAX, "12以上");
        list.add(bean6);
        return list;
    }

    //筛选写字楼工位
    public static List<WantFindBean> seatsList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean1 = new WantFindBean("0," + SEARCH_MAX, "不限");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("0,20", "0-20个");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("20,50", "20-50个");
        list.add(bean3);
        WantFindBean bean4 = new WantFindBean("50,100", "50-100个");
        list.add(bean4);
        WantFindBean bean5 = new WantFindBean("100,200", "100-200个");
        list.add(bean5);
        WantFindBean bean6 = new WantFindBean("200," + SEARCH_MAX, "200个以上");
        list.add(bean6);
        return list;
    }

    //筛选写字楼租金
    public static List<WantFindBean> rentJointList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean1 = new WantFindBean("0," + SEARCH_MAX, "不限");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("0,1000", "0-1000");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("1000,2000", "1000-2000");
        list.add(bean3);
        WantFindBean bean4 = new WantFindBean("2000,3000", "2000-3000");
        list.add(bean4);
        WantFindBean bean5 = new WantFindBean("3000,5000", "3000-5000");
        list.add(bean5);
        WantFindBean bean6 = new WantFindBean("5000," + SEARCH_MAX, "5000以上");
        list.add(bean6);
        return list;
    }

    //筛选写字楼工位
    public static List<WantFindBean> seatsJointList() {
        List<WantFindBean> list = new ArrayList<>();
        WantFindBean bean1 = new WantFindBean("0," + SEARCH_MAX, "不限");
        list.add(bean1);
        WantFindBean bean2 = new WantFindBean("0,5", "0-5个");
        list.add(bean2);
        WantFindBean bean3 = new WantFindBean("5,10", "5-10个");
        list.add(bean3);
        WantFindBean bean4 = new WantFindBean("10,20", "10-20个");
        list.add(bean4);
        WantFindBean bean5 = new WantFindBean("20,50", "20-50个");
        list.add(bean5);
        WantFindBean bean6 = new WantFindBean("50," + SEARCH_MAX, "50个以上");
        list.add(bean6);
        return list;
    }


}
