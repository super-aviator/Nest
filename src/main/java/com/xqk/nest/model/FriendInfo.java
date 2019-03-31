package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

public class FriendInfo {
    @JSONField(ordinal = 1)
    private String username;

    @JSONField(ordinal = 2,serializeUsing = ToStringSerializer.class)
    private Long id;

    @JSONField(ordinal = 5)
    private String status;

    @JSONField(ordinal = 4)
    private String sign;

    @JSONField(ordinal = 3)
    private String avatar;

    public FriendInfo(Long id, String username, String sign, String status, String avatar) {
        this.username = username;
        this.id = id;
        this.sign = sign;
        this.status = status;
        this.avatar = avatar;
    }

    //因为数据库中BIGINT查询出来可能会是Integer类型，所以加上强转
    public FriendInfo(Integer id, String username, String sign, String status, String avatar) {
        this.username = username;
        this.id = (long)id;
        this.sign = sign;
        this.status = status;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getSign() {
        return sign;
    }

    public String getStatus() {
        return status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "FriendInfo{" +
                "username='" + username + '\'' +
                ", id=" + id +
                ", sign='" + sign + '\'' +
                ", status='" + status + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
