package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class HistoryMsg {
    @JSONField(ordinal = 1)
    private int code;

    @JSONField(ordinal = 2)
    private String msg;

    @JSONField(ordinal = 3)
    private List<HistoryMsgItem> data;

    public HistoryMsg(int code, String msg, List<HistoryMsgItem> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public HistoryMsg() {
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

    public List<HistoryMsgItem> getData() {
        return data;
    }

    public void setData(List<HistoryMsgItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HistoryMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
