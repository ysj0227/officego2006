package com.officego.test;

import java.util.List;

/**
 * @author ysj
 * @date 2021/5/21
 * @description
 **/
public class RxUnitBean {

    private int status;
    private String message;
    private List<Data> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class  Data{
        private String dictCname;

        public String getDictCname() {
            return dictCname;
        }

        public void setDictCname(String dictCname) {
            this.dictCname = dictCname;
        }
    }

}
