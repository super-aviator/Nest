package com.xqk.nest.dto;

public class HistoryMessagePagableDTO {
    private String type;
    private long id;
    private long revId;
    private int start;
    private int limit;

    public HistoryMessagePagableDTO() {
    }

    public HistoryMessagePagableDTO(long id, long revId, String type, int start, int limit) {
        this.type = type;
        this.id = id;
        this.revId = revId;
        this.start = start;
        this.limit = limit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRevId() {
        return revId;
    }

    public void setRevId(long revId) {
        this.revId = revId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "HistoryMessagePagableDTO{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", revId=" + revId +
                ", start=" + start +
                ", limit=" + limit +
                '}';
    }
}