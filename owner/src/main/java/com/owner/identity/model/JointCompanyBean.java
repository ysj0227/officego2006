package com.owner.identity.model;

/**
 * Created by shijie
 * Date 2020/10/16
 **/
public class JointCompanyBean {
    private String companyText;
    private int flay;//(0 初始化 1 add  2 delete)

    public JointCompanyBean(String companyText, int flay) {
        this.companyText = companyText;
        this.flay = flay;
    }

    public String getCompanyText() {
        return companyText;
    }

    public void setCompanyText(String companyText) {
        this.companyText = companyText;
    }

    public int getFlay() {
        return flay;
    }

    public void setFlay(int flay) {
        this.flay = flay;
    }
}
