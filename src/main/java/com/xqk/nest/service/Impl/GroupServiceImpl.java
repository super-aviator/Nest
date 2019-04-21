package com.xqk.nest.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.dto.GroupMembersReturnDTO;
import com.xqk.nest.dto.MembersDTO;
import com.xqk.nest.service.GroupService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

@Component
public class GroupServiceImpl implements GroupService {
    /**
     * 用于显示群成员的详细信息
     *
     * @param id
     * @return
     */
    public String getMembers(long id) {
        GroupMembersReturnDTO msg = new GroupMembersReturnDTO();
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            MembersDTO members = session.selectOne("mapper.getGroupMembersMapper", id);
            msg.setMembers(members);
            return JSON.toJSONString(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new GroupMembersReturnDTO(1, "(: 服务器错误"));
        }
    }
}