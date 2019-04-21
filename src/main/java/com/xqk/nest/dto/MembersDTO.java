package com.xqk.nest.dto;

import java.util.List;

public class MembersDTO {
    private List<GroupMemberInfoDTO> list;

    public MembersDTO() {
    }

    public List<GroupMemberInfoDTO> getList() {
        return list;
    }

    public void setList(List<GroupMemberInfoDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "MembersDTO{" +
                "list=" + list +
                '}';
    }
}
