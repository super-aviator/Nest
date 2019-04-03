package com.xqk.nest.dao;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dto.UserDTO;
import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.model.*;
import org.apache.ibatis.session.SqlSession;

public class UserDAO implements UserDTO {

    public String getUserInfo(long userId) {
        UserInfoMsg msg = new UserInfoMsg(0);//消息pojo
        Data data = new Data();

        SqlSession s = MySqlSessionFactory.getSqlSession();
        try {
            data.setMine(s.<UserInfo>selectOne("mapper.SelectUserInfo", userId));
            data.setGroup(s.<GroupInfo>selectList("mapper.SelectGroupInfo", userId));
            data.setFriend(s.<PacketInfo>selectList("mapper.SelectFriendInfo", userId));

            msg.setData(data);
            return JSON.toJSONString(msg);

        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new UserInfoMsg(1, "(: 服务器错误"));
        } finally {
            if (s != null)
                s.close();
        }
    }

    @Override
    public void changeUserStatus(long id, String status) {
        SqlSession session = MySqlSessionFactory.getSqlSession();
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setStatus(status);
            userInfo.setId(id);
            session.update("mapper.changeUserStatus", userInfo);
            session.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void changeUserSign(long id, String sign) {
        SqlSession session = MySqlSessionFactory.getSqlSession();
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setSign(sign);
            userInfo.setId(id);
            session.update("mapper.changeUserSign", userInfo);
            session.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
