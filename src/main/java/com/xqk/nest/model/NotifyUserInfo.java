package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

public class NotifyUserInfo {
    @JSONField(ordinal = 1)
    private long id;

    @JSONField(ordinal = 2)
    private String avatar;

    @JSONField(ordinal = 3)
    private String username;

    @JSONField(ordinal = 4)
    private String sign;

    public NotifyUserInfo() {
    }

    public NotifyUserInfo(long id, String avatar, String username, String sign) {
        this.id = id;
        this.avatar = avatar;
        this.username = username;
        this.sign = sign;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "NotifyUserInfo{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
