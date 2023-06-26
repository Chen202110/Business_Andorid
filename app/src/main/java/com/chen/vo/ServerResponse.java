package com.chen.vo;


/**
 * 封装前端返回的同一实体类
 * */

public class ServerResponse<T> {

    private int status;//状态0，接口调用成功
    private T date;//当status=0时，将返回的数据封装到data中
    private String msg;//当status≠0时的提示信息

    public ServerResponse() {}



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
