package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfoMsg {
    @JSONField(ordinal = 1)
    private int code=0;

    @JSONField(ordinal = 2)
    private String msg="";

    @JSONField(ordinal = 3)
    private Data data;

    public UserInfoMsg() {
    }

    public UserInfoMsg(int code) {
        this.code = code;
    }

    public UserInfoMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public UserInfoMsg(int code, String msg, Data data) {

        this.code = code;
        this.msg = msg;
        this.data=data;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
