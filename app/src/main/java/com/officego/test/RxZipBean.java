package com.officego.test;

import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.LoginBean;

import java.util.List;

/**
 * @author ysj
 * @date 2021/5/21
 * @description
 **/
public class RxZipBean {
    private LoginBean bean;
    private List<DirectoryBean.DataBean> dataBeanList;

    public LoginBean getBean() {
        return bean;
    }

    public void setBean(LoginBean bean) {
        this.bean = bean;
    }

    public List<DirectoryBean.DataBean> getDataBeanList() {
        return dataBeanList;
    }

    public void setDataBeanList(List<DirectoryBean.DataBean> dataBeanList) {
        this.dataBeanList = dataBeanList;
    }
}
