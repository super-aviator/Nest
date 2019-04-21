package com.xqk.nest.websocket.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class StatusMessageDTO {
    @JSONField(ordinal = 1)
    private long id;

    @JSONField(ordinal = 2)
    private String status;

    public StatusMessageDTO() {
    }

    public StatusMessageDTO(long id, String status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusMessageDTO{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
