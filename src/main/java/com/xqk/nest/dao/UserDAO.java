package com.xqk.nest.dao;

import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.dto.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {

    public CommonReturnDTO<DataDTO> getUserInfo(long userId) {
        CommonReturnDTO<DataDTO> msg;//消息pojo

        try (SqlSession s = MySqlSessionFactory.getSqlSession()) {
            DataDTO dataDTO = new DataDTO();
            dataDTO.setMine(s.selectOne("mapper.SelectUserInfo", userId));
            dataDTO.setGroup(s.selectList("mapper.SelectGroupInfo", userId));
            dataDTO.setFriend(s.selectList("mapper.SelectFriendInfo", userId));
            msg = new CommonReturnDTO<>(0, "", dataDTO);
        }
        return msg;
    }

    public Tuple<UserInfoDTO, GroupInfoDTO> getUserList(String username) {
        Tuple<UserInfoDTO, GroupInfoDTO> tuple = new Tuple<>();
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            UserInfoDTO userInfo = session.selectOne("mapper.getUserList", username);
            GroupInfoDTO groupInfoDTO = session.selectOne("mapper.getGroupList", username);
            tuple.setT(userInfo);
            tuple.setE(groupInfoDTO);
            return tuple;
        }
    }

    public void changeUserSign(long id, String sign) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            UserInfoDTO userInfo = new UserInfoDTO();
            userInfo.setSign(sign);
            userInfo.setId(id);
            session.update("mapper.changeUserSign", userInfo);
            session.commit();
        }
    }

    public void changeUserStatus(long id, String status) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            UserInfoDTO userInfo = new UserInfoDTO();
            userInfo.setStatus(status);
            userInfo.setId(id);
            session.update("mapper.changeUserStatus", userInfo);
            session.commit();
        } catch (Exception e) {
            throw e;
        }
    }

    private void addFriend(long userId, long packetId) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            session.insert("mapper.addFriend", new Tuple<>(userId, packetId));
            session.commit();
        }
    }

    public void addFriend(long id, long uid, long from_group, long group) {
        addFriend(id, from_group);
        addFriend(uid, group);
    }

    public UserInfoDTO getUser(long userId) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            return session.selectOne("mapper.getUser", userId);
        }
    }
}
