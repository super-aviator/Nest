package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

@SuppressWarnings("unused")
public class HistoryMsgItem {
    @JSONField(ordinal = 1)
    private String username;

    @JSONField(ordinal = 2)
    private long id;//发送方ID

    @JSONField(serialize = false)
    private long revId;//接收方ID不序列化

    @JSONField(serialize = false)
    private long type;//聊天类型不序列化

    @JSONField(serialize = false)
    private long offset;//分页起始地址

    @JSONField(serialize = false)
    private long count;//每页显示数


    @JSONField(ordinal = 3)
    private String avatar;

    @JSONField(ordinal = 4)
    private long timestamp;

    @JSONField(ordinal = 5)
    private String content;

    public HistoryMsgItem() {

    }

    public HistoryMsgItem(String username, long id, long revId, String avatar, long timestamp, String content,String type,long offset,long count) {
        this.username = username;
        this.id = id;
        this.revId = revId;
        this.avatar = avatar;
        this.timestamp = timestamp;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getRevId() {
        return revId;
    }

    @Override
    public String toString() {
        return "HistoryMsgItem{" +
                "username='" + username + '\'' +
                ", id=" + id +
                ", revId=" + revId +
                ", avatar='" + avatar + '\'' +
                ", timestamp=" + timestamp +
                ", content='" + content + '\'' +
                '}';
    }
}
