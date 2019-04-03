package com.xqk.nest.dao;

import com.xqk.nest.dto.AccountDTO;
import com.xqk.nest.config.MySqlSessionFactory;
import org.apache.ibatis.session.SqlSession;

public class AccountDAO implements AccountDTO {

    //0 登录成功 ，1 账号未注册，2密码错误
    @Override
    public int pwCorrect(String username, String password) {
        if (username == null || password == null) return 2;

        SqlSession session = null;
        String temp = null;
        try {
            session = MySqlSessionFactory.getSqlSession();
            temp = session.selectOne("mapper.hasSignup", username);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
        if (temp != null) {
            if (temp.equals(password))
                return 0;
            else return 2;
        }
        return 1;
    }

}
