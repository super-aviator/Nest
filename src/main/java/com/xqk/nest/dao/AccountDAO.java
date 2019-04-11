package com.xqk.nest.dao;

import com.xqk.nest.dto.AccountDTO;
import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.model.CommonReturnModel;
import com.xqk.nest.model.UserInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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

    //1 用户名已经注册 2 注册失败 3 注册成功
    @Override
    public CommonReturnModel<Integer> signUp(String username, String password, MultipartFile img, String sign) {
        CommonReturnModel<Integer> result;

        if (hasSingUp(username))
            return new CommonReturnModel<>(0,"",1);

        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {
            File f = new File("D:\\Nest\\web\\WEB-INF\\Nest\\pages\\dataImg" + img.getOriginalFilename());
            if (!f.exists())
                img.transferTo(f);
            String avatar = "./dataImg/" + img.getOriginalFilename();

            UserInfo userInfo = new UserInfo(username, sign, avatar, password);
            int col = session.insert("mapper.singUp", userInfo);
            session.commit();
            result= new CommonReturnModel<>(0,"",3);
        } catch (Exception e) {
            e.printStackTrace();
            result= new CommonReturnModel<>(0,"",2);
        }
        return result;
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
