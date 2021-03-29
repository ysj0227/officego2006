package com.owner.pay;

/**
 * Created by shijie
 * Date 2021/3/29
 **/
public class RightsBean {
    private int drawable;
    private String title;
    private String des;

    public RightsBean(int drawable, String title, String des) {
        this.drawable = drawable;
        this.title = title;
        this.des = des;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
