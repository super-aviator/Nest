package com.xqk.nest.dao;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dto.GroupDTO;
import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.model.GroupMembersMsg;
import com.xqk.nest.model.Members;
import org.apache.ibatis.session.SqlSession;

public class GroupDAO implements GroupDTO {
    private GroupMembersMsg msg = new GroupMembersMsg();


    public String getMembers(long id) {
        GroupMembersMsg msg = new GroupMembersMsg();
        SqlSession session = null;
        try {
            session = MySqlSessionFactory.getSqlSession();
           Members members= session.selectOne("mapper.getGroupMembersMapper", id);
            msg.setMembers(members);
            return JSON.toJSONString(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new GroupMembersMsg(1, "服务器错误"));
        } finally {
            if (session != null)
                session.close();
        }
    }
}