package com.officego.ui.find;

/**
 * Created by shijie
 * Date 2020/12/24
 **/
public class WantFindBean {
    private String key;
    private String value;

    public WantFindBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
