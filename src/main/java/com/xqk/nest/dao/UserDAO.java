package com.xqk.nest.dao;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dto.UserDTO;
import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.model.*;
import org.apache.ibatis.session.SqlSession;

public class UserDAO implements UserDTO {

    public UserInfoMsg getUserInfo(long userId) {
        UserInfoMsg msg = new UserInfoMsg(0);//消息pojo
        Data data = new Data();

        try (SqlSession s = MySqlSessionFactory.getSqlSession()) {
            data.setMine(s.selectOne("mapper.SelectUserInfo", userId));
            data.setGroup(s.selectList("mapper.SelectGroupInfo", userId));
            data.setFriend(s.selectList("mapper.SelectFriendInfo", userId));

            msg.setData(data);
        }
        return msg;
    }

    @Override
    public void changeUserStatus(long id, String status) {

        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setStatus(status);
            userInfo.setId(id);
            session.update("mapper.changeUserStatus", userInfo);
            session.commit();
        }
    }

    @Override
    public void changeUserSign(long id, String sign) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setSign(sign);
            userInfo.setId(id);
            session.update("mapper.changeUserSign", userInfo);
            session.commit();
        }

    }

    @Override
    public Tuple<UserInfo,GroupInfo> getUserList(String username) {
        Tuple<UserInfo, GroupInfo> tuple = new Tuple<>();
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            UserInfo userInfo = session.selectOne("mapper.getUserList", username);
            GroupInfo groupInfo = session.selectOne("mapper.getGroupList", username);
            tuple.setT(userInfo);
            tuple.setE(groupInfo);
            return tuple;
        }
    }

    @Override
    public void addFriend(long userId, long packetId) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            session.insert("mapper.addFriend",new Tuple<>(userId,packetId));
            session.commit();
        }
    }

    /**
     * 通过id或者用户信息
     * @param userId
     * @return
     */
    @Override
    public UserInfo getUser(long userId) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            return session.selectOne("mapper.getUser",userId);
        }
    }

}
