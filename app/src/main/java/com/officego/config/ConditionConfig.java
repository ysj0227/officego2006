package com.officego.config;

import android.text.TextUtils;
import android.widget.TextView;

import com.officego.R;
import com.officego.commonlib.constant.Constants;
import com.officego.ui.home.model.ConditionBean;

/**
 * Created by YangShiJie
 * Data 2020/7/3.
 * Descriptions:
 **/
public class ConditionConfig {
    public static ConditionBean getConditionBean;

    public static void showText(TextView tvSearchOffice, int filterType) {
        if (filterType == Constants.SEARCH_ALL) {
            tvSearchOffice.setText(R.string.str_house_all);
        } else if (filterType == Constants.SEARCH_JOINT_WORK) {
            tvSearchOffice.setText(R.string.str_house_tenant);
        } else if (filterType == Constants.SEARCH_OPEN_SEATS) {
            tvSearchOffice.setText(R.string.str_house_open_seats);
        } else if (filterType == Constants.SEARCH_OFFICE) {
            tvSearchOffice.setText(R.string.str_house_office);
        } else if (filterType == Constants.SEARCH_GARDEN) {
            tvSearchOffice.setText(R.string.str_house_garden);
        }
    }

    public static ConditionBean setConditionBean(int filterType, int btype, String area, String dayPrice, String seats,
                                                 String decoration, String houseTags) {
        ConditionBean bean = new ConditionBean();
        String max = "0,99999999";
        int intMax = 99999999;
        if (filterType == Constants.SEARCH_OFFICE || filterType == Constants.SEARCH_GARDEN) {
            //面积 写字楼和园区
            if (TextUtils.equals("", area) || TextUtils.equals(max, area)) {
                area = "";
            } else {
                String start, end;
                if (area.contains(",")) {
                    String str1 = area.substring(0, area.indexOf(","));
                    start = area.substring(0, str1.length());
                    end = area.substring(str1.length() + 1);
                    if (Integer.parseInt(start) > 0 && Integer.parseInt(end) == intMax) {
                        bean.setAreaValue(start + "㎡以上");
                    } else {
                        bean.setAreaValue(start + "-" + end + "㎡");
                    }
                }
            }
        } else if (filterType == Constants.SEARCH_JOINT_WORK) {
            //工位
            if (TextUtils.equals("", seats) || TextUtils.equals(max, seats)) {
                seats = "";
                area = "";
            } else {
                String start, end;
                if (seats.contains(",")) {//工位
                    String str1 = seats.substring(0, seats.indexOf(","));
                    start = seats.substring(0, str1.length());
                    end = seats.substring(str1.length() + 1);
                    if (Integer.parseInt(start) > 0 && Integer.parseInt(end) == intMax) {
                        bean.setSeatsValue(start + "人以上");
                    } else {
                        bean.setSeatsValue(start + "-" + end + "人");
                    }
                }
            }
        }else {//开放工位或默认全部
            return null;
        }
        bean.setArea(area);
        bean.setSeats(seats);
        bean.setDayPrice(dayPrice);
        bean.setDecoration(decoration);
        bean.setHouseTags(houseTags);
        return bean;
    }
}
