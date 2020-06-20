package com.officego.db;

import org.litepal.crud.LitePalSupport;

/**
 * Created by yangshijie
 * Data 2020/5/7.
 **/
public class Area extends LitePalSupport {
    private String name;
    private int age;
    private boolean man;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMan() {
        return man;
    }

    public void setMan(boolean man) {
        this.man = man;
    }
}
