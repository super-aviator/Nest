package com.xqk.nest.model;

import java.util.List;

public class PacketInfo {
    private String groupname;

    private Long id;

    private List<FriendInfo> list;

    public PacketInfo(Long id,String groupname) {
        this.groupname = groupname;
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FriendInfo> getList() {
        return list;
    }

    public void setList(List<FriendInfo> list) {
        this.list = list;
    }
}
