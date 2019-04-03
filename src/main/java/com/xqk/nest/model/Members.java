package com.xqk.nest.model;

import java.util.List;

public class Members{
    private List<GroupMemberInfo> list;

    public Members() {
    }

    public List<GroupMemberInfo> getList() {
        return list;
    }

    public void setList(List<GroupMemberInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Members{" +
                "list=" + list +
                '}';
    }
}
