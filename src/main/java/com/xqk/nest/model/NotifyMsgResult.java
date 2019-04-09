package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class NotifyMsgResult {
    @JSONField(ordinal = 1)
    private int code;
    @JSONField(ordinal = 2)
    private int pages=1;
    @JSONField(ordinal = 3)
    private List<NotifyMsg> data;

    public NotifyMsgResult() {
    }

    public NotifyMsgResult(int code, int pages, List<NotifyMsg> notifyMsg) {
        this.code = code;
        this.pages = pages;
        this.data = notifyMsg;
    }

    public NotifyMsgResult(int code, List<NotifyMsg> notifyMsg) {
        this.code = code;
        this.data = notifyMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<NotifyMsg> getData() {
        return data;
    }

    public void setData(List<NotifyMsg> notifyMsg) {
        this.data = notifyMsg;
    }

    @Override
    public String toString() {
        return "NotifyMsgResult{" +
                "code=" + code +
                ", pages=" + pages +
                ", notifyMsg=" + data +
                '}';
    }
}
