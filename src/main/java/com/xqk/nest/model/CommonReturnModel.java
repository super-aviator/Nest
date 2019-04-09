package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

public class CommonReturnModel<T> {
    @JSONField(ordinal = 1)
    private int code;

    @JSONField(ordinal = 2)
    private String msg;

    @JSONField(ordinal = 3)
    private T data;

    public CommonReturnModel(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CommonReturnModel(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonReturnModel() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonReturnModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
