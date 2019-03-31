package com.xqk.nest.model;

import com.alibaba.fastjson.annotation.JSONField;

public class GroupMembersMsg {
    @JSONField(ordinal = 1)
    private int code=0;

    @JSONField(ordinal = 2)
    private String msg="";

    @JSONField(ordinal = 3,name = "data")
    private Members members;

    public GroupMembersMsg(int code, String msg, Members data) {
        this.code = code;
        this.msg = msg;
        this.members = data;
    }

    public GroupMembersMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public GroupMembersMsg() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }
}
