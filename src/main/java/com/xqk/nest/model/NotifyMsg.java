package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

public class NotifyMsg{
    @JSONField(ordinal = 1)
    private long id;

    @JSONField(ordinal = 2)
    private String content;

    @JSONField(ordinal = 3)
    private long uid;

    @JSONField(ordinal = 4)
    private long from;

    @JSONField(ordinal = 5)
    private long from_group;

    @JSONField(ordinal = 6)
    private long type;

    @JSONField(ordinal = 7)
    private String remark;

    @JSONField(ordinal = 8)
    private Object href;

    @JSONField(ordinal = 9)
    private long read;

    @JSONField(ordinal = 10)
    private String time;

    @JSONField(ordinal = 11)
    private NotifyUserInfo user;


    public NotifyMsg() {
    }

    public NotifyMsg(long id, String content, long uid, long from, long from_group, long type, String remark, Object href, long read, String time, NotifyUserInfo user) {
        this.id = id;
        this.content = content;
        this.uid = uid;
        this.from = from;
        this.from_group = from_group;
        this.type = type;
        this.remark = remark;
        this.href = href;
        this.read = read;
        this.time = time;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getFrom_group() {
        return from_group;
    }

    public void setFrom_group(long from_group) {
        this.from_group = from_group;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getHref() {
        return href;
    }

    public void setHref(Object href) {
        this.href = href;
    }

    public long getRead() {
        return read;
    }

    public void setRead(long read) {
        this.read = read;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public NotifyUserInfo getUser() {
        return user;
    }

    public void setUser(NotifyUserInfo user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "NotifyMsg{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", uid=" + uid +
                ", from=" + from +
                ", from_group=" + from_group +
                ", type=" + type +
                ", remark='" + remark + '\'' +
                ", href='" + href + '\'' +
                ", read=" + read +
                ", time='" + time + '\'' +
                ", user=" + user +
                '}';
    }
}
