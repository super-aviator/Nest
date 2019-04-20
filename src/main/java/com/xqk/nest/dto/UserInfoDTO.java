package com.xqk.nest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

public class UserInfoDTO {
    @JSONField(ordinal = 1)
    private String username;

    @JSONField(ordinal = 2,serializeUsing = ToStringSerializer.class)
    private long id;

    @JSONField(ordinal = 3)
    private String status;

    @JSONField(ordinal = 4)
    private String sign;

    @JSONField(ordinal = 5)
    private String avatar;

    @JSONField(serialize = false)
    private String password;

    public UserInfoDTO(long id, String username, String sign, String status, String avatar) {
        this.username = username;
        this.id = id;
        this.sign = sign;
        this.status = status;
        this.avatar = avatar;
    }

    public UserInfoDTO(String username, String sign, String avatar, String password) {
        this.username = username;
        this.sign = sign;
        this.password = password;
        this.avatar = avatar;
    }

    public UserInfoDTO() {
    }

    //因为数据库中BIGINT查询出来可能会是Integer类型，所以加上强转
    public UserInfoDTO(Integer id, String username, String sign, String status, String avatar) {
        this.username = username;
        this.id = (long)id;
        this.sign = sign;
        this.status = status;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public long getId() {
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

    public void setId(long id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "username='" + username + '\'' +
                ", id=" + id +
                ", sign='" + sign + '\'' +
                ", status='" + status + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
