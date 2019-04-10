package com.xqk.nest.contorller;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dao.AccountDAO;
import com.xqk.nest.model.Tuple;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
            if (!dao.hasSingUp(username)) {
                Tuple<String, Long> tuple = new Tuple<>("账号未注册", -1L);
                response.getWriter().write(JSON.toJSONString(tuple));
                return;
            }

            int result = dao.pwCorrect(username, password);
            Tuple<String, Long> tuple = new Tuple<>();
            switch (result) {
                case 0:
                    tuple.setT("登录成功");
                    break;
                case 1:
                    tuple.setT("账号未注册");
                    break;
                case 2:
                    tuple.setT("密码错误");
                    break;
            }
            tuple.setE(dao.getUserId(username));
            response.getWriter().write(JSON.toJSONString(tuple));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ResponseBody
    public void singUp(@RequestParam("username") String username, @RequestParam("password") String password
            , @RequestParam("sign") String sign, @RequestParam("avatar") MultipartFile img, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        File f = new File("E:\\Nest\\web\\WEB-INF\\Nest\\pages\\dataImg" + img.getOriginalFilename());
        img.transferTo(f);
        String avatar = "./dataImg/" + img.getOriginalFilename();

        if (dao.hasSingUp(username)) {
            response.getWriter().write("用户名已经注册");
            return;
        }

        if (dao.signUp(username, password, avatar, sign) == 1)
            response.getWriter().write("注册成功");
        else
            response.getWriter().write("注册失败");
    }

}
