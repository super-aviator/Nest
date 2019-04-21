package com.xqk.nest.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

@SuppressWarnings("unused")
public class DataDTO {
    @JSONField(ordinal = 1)
    private UserInfoDTO mine;

    @JSONField(ordinal = 2)
    private List<PacketInfoDTO> friend;

    @JSONField(ordinal = 3)
    private List<GroupInfoDTO> group;

    @JSONField(name = "mine")
    public UserInfoDTO getMine() {
        return mine;
    }

    @JSONField(name = "mine")
    public void setMine(UserInfoDTO mine) {
        this.mine = mine;
    }

    public DataDTO() {
    }

    public DataDTO(UserInfoDTO mine, List<PacketInfoDTO> friend, List<GroupInfoDTO> group) {
        this.mine = mine;
        this.friend = friend;
        this.group = group;
    }

    public List<PacketInfoDTO> getFriend() {
        return friend;
    }

    public void setFriend(List<PacketInfoDTO> friend) {
        this.friend = friend;
    }

    public List<GroupInfoDTO> getGroup() {
        return group;
    }

    public void setGroup(List<GroupInfoDTO> group) {
        this.group = group;
    }
}
