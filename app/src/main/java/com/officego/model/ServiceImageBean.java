package com.officego.model;

/**
 * Created by YangShiJie
 * Data 2020/5/14.
 * Descriptions:
 **/
public class ServiceImageBean {
    private String url;
    private String name;

    public ServiceImageBean(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
