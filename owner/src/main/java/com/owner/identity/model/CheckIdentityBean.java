package com.owner.identity.model;

public class CheckIdentityBean {

    /**
     * explain : 可以创建该网点
     * flag : 0   0不存在1存在
     */

    private String explain;
    private int flag;

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
