package com.xqk.nest.dao;

import com.xqk.nest.dto.AccountDTO;
import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.model.UserInfo;
import org.apache.ibatis.session.SqlSession;

public class AccountDAO implements AccountDTO {

    //0 登录成功 ，1 账号未注册，2密码错误
    @Override
    public int pwCorrect(String username, String password) {
        if (username == null || password == null) return 2;
        String temp = null;
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            temp = session.selectOne("mapper.hasSignup", username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (temp != null && !temp.equals("")) {
            if (temp.equals(password))
                return 0;
            else return 2;
        }
        return 1;
    }

    @Override
    public long getUserId(String username) {
        if (username == null) return -1;
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            return session.selectOne("mapper.getUserId", username);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public int signUp(String username, String password, String avatar, String sign) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            UserInfo userInfo = new UserInfo(username, sign, avatar, password);
            int col = session.insert("mapper.singUp", userInfo);
            session.commit();
            return col;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean hasSingUp(String username) {
        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            Integer i = session.selectOne("mapper.hasSignUp", username);
            return i != null && i.equals(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
