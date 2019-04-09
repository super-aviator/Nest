package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

public class UploadImageReturnMod {
    @JSONField(ordinal = 1)
    private int code=0;

    @JSONField(ordinal = 2)
    private String msg="";

    @JSONField(ordinal = 3)
    private UploadImageMod data;

    public UploadImageReturnMod(int code, String msg, UploadImageMod data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public UploadImageReturnMod() {
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

    public UploadImageMod getData() {
        return data;
    }

    public void setData(UploadImageMod data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UploadImageReturnMod{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
