package com.officego.ui.home.model;

import java.io.Serializable;

/**
 * Created by YangShiJie
 * Data 2020/6/5.
 * Descriptions:
 **/
public class ConditionBean implements Serializable {
    private String area;
    private String areaValue;
    private String seats;
    private String seatsValue;
    private String dayPrice;
    private String decoration;
    private String houseTags;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(String dayPrice) {
        this.dayPrice = dayPrice;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }

    public String getHouseTags() {
        return houseTags;
    }

    public void setHouseTags(String houseTags) {
        this.houseTags = houseTags;
    }

    public String getAreaValue() {
        return areaValue;
    }

    public void setAreaValue(String areaValue) {
        this.areaValue = areaValue;
    }

    public String getSeatsValue() {
        return seatsValue;
    }

    public void setSeatsValue(String seatsValue) {
        this.seatsValue = seatsValue;
    }
}
