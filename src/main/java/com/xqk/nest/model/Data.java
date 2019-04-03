package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

@SuppressWarnings("unused")
public class Data {
    @JSONField(ordinal = 1)
    private UserInfo mine;

    @JSONField(ordinal = 2)
    private List<PacketInfo> friend;

    @JSONField(ordinal = 3)
    private List<GroupInfo> group;

    @JSONField(name = "mine")
    public UserInfo getMine() {
        return mine;
    }

    @JSONField(name = "mine")
    public void setMine(UserInfo mine) {
        this.mine = mine;
    }

    public Data() {
    }

    public Data(UserInfo mine, List<PacketInfo> friend, List<GroupInfo> group) {
        this.mine = mine;
        this.friend = friend;
        this.group = group;
    }

    public List<PacketInfo> getFriend() {
        return friend;
    }

    public void setFriend(List<PacketInfo> friend) {
        this.friend = friend;
    }

    public List<GroupInfo> getGroup() {
        return group;
    }

    public void setGroup(List<GroupInfo> group) {
        this.group = group;
    }
}
