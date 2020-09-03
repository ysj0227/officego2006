package com.officego.db;

import org.litepal.crud.LitePalSupport;

/**
 * Created by yangshijie
 * Data 2020/5/7.
 **/
public class InsertHouseInfo extends LitePalSupport {
    private int id;
    private String houseValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHouseValue() {
        return houseValue;
    }

    public void setHouseValue(String houseValue) {
        this.houseValue = houseValue;
    }
}
