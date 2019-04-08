package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

public class AddFriendMsg {
    @JSONField(ordinal = 1)
    private String type;

    @JSONField(ordinal = 2)
    private String avatar;

    @JSONField(ordinal = 3)
    private String username;

    @JSONField(ordinal = 4)
    private long groupid;

    @JSONField(ordinal = 5,serializeUsing = ToStringSerializer.class)
    private Long id;

    @JSONField(ordinal = 6)
    private String sign;

    public AddFriendMsg() {
    }

    public AddFriendMsg(String type,long groupid,UserInfo userInfo) {
        this.type = type;
        this.groupid=groupid;
        this.id=userInfo.getId();
        this.avatar=userInfo.getAvatar();
        this.sign=userInfo.getSign();
        this.username=userInfo.getUsername();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public long getGroupid() {
        return groupid;
    }

    public void setGroupid(long groupid) {
        this.groupid = groupid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "AddFriendMsg{" +
                "type='" + type + '\'' +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", groupid=" + groupid +
                ", id=" + id +
                ", sign='" + sign + '\'' +
                '}';
    }
}
