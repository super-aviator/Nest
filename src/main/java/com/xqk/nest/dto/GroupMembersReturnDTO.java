package com.xqk.nest.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class GroupMembersReturnDTO {
    @JSONField(ordinal = 1)
    private int code=0;

    @JSONField(ordinal = 2)
    private String msg="";

    @JSONField(ordinal = 3,name = "data")
    private MembersDTO members;

    public GroupMembersReturnDTO(int code, String msg, MembersDTO data) {
        this.code = code;
        this.msg = msg;
        this.members = data;
    }

    public GroupMembersReturnDTO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public GroupMembersReturnDTO() {
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

    public MembersDTO getMembers() {
        return members;
    }

    public void setMembers(MembersDTO members) {
        this.members = members;
    }
}
