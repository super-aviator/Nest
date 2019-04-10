package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class NotifyReturnModel {
    @JSONField(ordinal = 1)
    private int code;
    @JSONField(ordinal = 2)
    private int pages=1;
    @JSONField(ordinal = 3)
    private List<NotifyModel> data;

    public NotifyReturnModel() {
    }

    public NotifyReturnModel(int code, int pages, List<NotifyModel> notifyModel) {
        this.code = code;
        this.pages = pages;
        this.data = notifyModel;
    }

    public NotifyReturnModel(int code, List<NotifyModel> notifyModel) {
        this.code = code;
        this.data = notifyModel;
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

    public List<NotifyModel> getData() {
        return data;
    }

    public void setData(List<NotifyModel> notifyModel) {
        this.data = notifyModel;
    }

    @Override
    public String toString() {
        return "NotifyReturnModel{" +
                "code=" + code +
                ", pages=" + pages +
                ", notifyMsg=" + data +
                '}';
    }
}
