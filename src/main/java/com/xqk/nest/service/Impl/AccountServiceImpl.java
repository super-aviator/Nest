package com.xqk.nest.service.Impl;

import com.xqk.nest.config.MySqlSessionFactory;
import com.xqk.nest.dto.CommonReturnDTO;
import com.xqk.nest.dto.UserInfoDTO;
import com.xqk.nest.service.AccountService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class AccountServiceImpl implements AccountService {

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
    public CommonReturnDTO<Integer> signUp(String username, String password, MultipartFile img, String sign) {
        CommonReturnDTO<Integer> result;

        if (hasSingUp(username))
            return new CommonReturnDTO<>(0,"用户已注册",1);

        try (SqlSession session = MySqlSessionFactory.getSqlSession()) {

            File file = new File("C:\\Users\\Aviator\\Desktop\\毕业设计实现\\Nest\\web\\WEB-INF\\Nest\\pages\\dataImg\\" + img.getOriginalFilename());
            if (!file.exists())
                img.transferTo(file);

            String avatar = "http://127.0.0.1:5500/pages/dataImg/" + img.getOriginalFilename();

            UserInfoDTO userInfo = new UserInfoDTO(username, sign, avatar, password);
            session.insert("mapper.singUp", userInfo);
            session.commit();
            result= new CommonReturnDTO<>(0,"注册成功",3);
        } catch (Exception e) {
            e.printStackTrace();
            result= new CommonReturnDTO<>(0,"注册失败",2);
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
