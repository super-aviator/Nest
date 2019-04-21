package com.xqk.nest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

public class GroupInfoDTO {
    @JSONField(ordinal = 1)
    private String groupname;

    @JSONField(ordinal = 2,serializeUsing = ToStringSerializer.class)
    private long id;

    @JSONField(ordinal = 3)
    private String avatar;

    public GroupInfoDTO(String groupname, long id, String avatar) {
        this.groupname = groupname;
        this.id = id;
        this.avatar = avatar;
    }

    public GroupInfoDTO() {
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
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

    @Override
    public String toString() {
        return "GroupInfoDTO{" +
                "groupname='" + groupname + '\'' +
                ", id=" + id +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
