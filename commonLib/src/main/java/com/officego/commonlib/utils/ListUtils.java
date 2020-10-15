package com.officego.commonlib.utils;

import android.text.TextUtils;

import com.officego.commonlib.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/11.
 * Descriptions:
 **/
public class ListUtils {

    public static List<String> yearList() {
        List<String> list = new ArrayList<>();
        int year = DateTimeUtils.getYear(Calendar.getInstance().getTime());
        list.add(year + "");
        list.add((year + 1) + "");
        return list;
    }

    public static List<String> monthList() {
        List<String> list = new ArrayList<>();
        list.add("01");
        list.add("02");
        list.add("03");
        list.add("04");
        list.add("05");
        list.add("06");
        list.add("07");
        list.add("08");
        list.add("09");
        list.add("10");
        list.add("11");
        list.add("12");
        return list;
    }

    //根据年月获取天数
    public static List<String> dayList(String year, String month) {
        int dayNum;
        if (TextUtils.isEmpty(year) || TextUtils.isEmpty(month)) {
            dayNum = 30;
        } else {
            dayNum = DateTimeUtils.getCurrentMonthToDays(year, month);
        }
        List<String> list = new ArrayList<>();
        for (int i = 1; i < dayNum + 1; i++) {
            if (i <= 9) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    public static List<String> hoursList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list.add("" + i);
        }
        return list;
    }

    public static List<String> minuteList() {
        List<String> list = new ArrayList<>();
        list.add("00");
        list.add("15");
        list.add("30");
        list.add("45");
        list.add("55");
        return list;
    }

    public static List<String> yearSetList() {
        List<String> list = new ArrayList<>();
        int year = DateTimeUtils.getYear(Calendar.getInstance().getTime());
        for (int i = 0; i < (year - 1984); i++) {
            list.add((1985 + i) + "");
        }
        return list;
    }

}
