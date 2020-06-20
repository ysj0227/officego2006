package com.officego.commonlib.retrofit;

/**
 * Description:响应基类
 * Created by bruce on 2019/2/27.
 */
public class BaseResponse<T> {

//                 "status": 200,
//                "message": "success",
//                "data": "MV9zdW53ZWxsXzE1ODc1Mzg5ODA=", //token
//                "totalCount": null

    private int status;
    private String message;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
