package com.xqk.nest.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class NotifyReturnDTO {
    @JSONField(ordinal = 1)
    private int code;
    @JSONField(ordinal = 2)
    private int pages=1;
    @JSONField(ordinal = 3)
    private List<NotifyDTO> data;

    public NotifyReturnDTO() {
    }

    public NotifyReturnDTO(int code, int pages, List<NotifyDTO> notifyDTO) {
        this.code = code;
        this.pages = pages;
        this.data = notifyDTO;
    }

    public NotifyReturnDTO(int code, List<NotifyDTO> notifyDTO) {
        this.code = code;
        this.data = notifyDTO;
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

    public List<NotifyDTO> getData() {
        return data;
    }

    public void setData(List<NotifyDTO> notifyDTO) {
        this.data = notifyDTO;
    }

    @Override
    public String toString() {
        return "NotifyReturnDTO{" +
                "code=" + code +
                ", pages=" + pages +
                ", notifyMsg=" + data +
                '}';
    }
}
