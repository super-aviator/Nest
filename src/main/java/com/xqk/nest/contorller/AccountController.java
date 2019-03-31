package com.xqk.nest.contorller;

import com.xqk.nest.dao.AccountDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/sign")
public class AccountController {
    private AccountDAO dao = new AccountDAO();

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public void singIn(@RequestParam("username") String username, @RequestParam("password") String password
            , HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");

        try {
            int result = dao.pwCorrect(username, password);
            switch (result) {
                case 0:
                    response.getWriter().write("登录成功");
                    break;
                case 1:
                    response.getWriter().write("账号未注册");
                    break;
                case 2:
                    response.getWriter().write("密码错误");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
