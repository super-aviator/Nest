package com.xqk.nest.websocket.model;

import com.alibaba.fastjson.annotation.JSONField;

@SuppressWarnings("unused")
public class HistoryChatMessage {
    @JSONField(serialize = false)
    private String type;

    @JSONField(ordinal = 1)
    private String username;

    @JSONField(ordinal = 3)
    private String avatar;

    @JSONField(ordinal = 2)
    private String id;//接收者id

    @JSONField(serialize = false)
    private String revId;

    @JSONField(ordinal = 5)
    private String content;

    @JSONField(ordinal = 4)
    private long timestamp;

    public HistoryChatMessage() {
    }

    public HistoryChatMessage(String username, String avatar, String id,String revId, String content, long timestamp,String type) {
        this.username = username;
        this.avatar = avatar;
        this.id = id;
        this.revId=revId;
        this.content = content;
        this.timestamp = timestamp;
        this.type=type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
