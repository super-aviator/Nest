package com.xqk.nest.dto;

import java.util.List;

public class PacketInfoDTO {
    private String groupname;

    private Long id;

    private List<FriendInfoDTO> list;

    public PacketInfoDTO(Long id, String groupname) {
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

    public List<FriendInfoDTO> getList() {
        return list;
    }

    public void setList(List<FriendInfoDTO> list) {
        this.list = list;
    }
}
