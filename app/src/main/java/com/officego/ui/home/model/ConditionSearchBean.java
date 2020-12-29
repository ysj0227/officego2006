package com.officego.ui.home.model;

import java.io.Serializable;

/**
 * Created by shijie
 * Date 2020/12/29
 **/
public class ConditionSearchBean implements Serializable {
    private String area;
    private String rent;
    private String seats;
    private String decoration;
    private String brand;
    private String unique;
    private boolean isVr;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public boolean isVr() {
        return isVr;
    }

    public void setVr(boolean vr) {
        isVr = vr;
    }
}
