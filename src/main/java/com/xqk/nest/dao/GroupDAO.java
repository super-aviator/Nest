package com.xqk.nest.dao;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dto.GroupDTO;
import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.model.GroupMembersReturnModel;
import com.xqk.nest.model.Members;
import org.apache.ibatis.session.SqlSession;

public class GroupDAO implements GroupDTO {
    /**
     * 用于显示群成员的详细信息
     *
     * @param id
     * @return
     */
    public String getMembers(long id) {
        GroupMembersReturnModel msg = new GroupMembersReturnModel();
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            Members members = session.selectOne("mapper.getGroupMembersMapper", id);
            msg.setMembers(members);
            return JSON.toJSONString(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new GroupMembersReturnModel(1, "(: 服务器错误"));
        }
    }
}