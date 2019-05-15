package com.xqk.nest.contorller;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dto.UploadImageDTO;
import com.xqk.nest.service.AccountService;
import com.xqk.nest.dto.CommonReturnDTO;
import com.xqk.nest.dto.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/sign")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public void singIn(@RequestParam("username") String username, @RequestParam("password") String password
            , HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");

        try {
            if (!accountService.hasSingUp(username)) {
                Tuple<String, Long> tuple = new Tuple<>("账号未注册", -1L);
                response.getWriter().write(JSON.toJSONString(tuple));
                return;
            }

            int result = accountService.pwCorrect(username, password);
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
            tuple.setE(accountService.getUserId(username));
            response.getWriter().write(JSON.toJSONString(tuple));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public void singUp(@RequestParam("username") String username, @RequestParam("password") String password
            , @RequestParam("sign") String sign, @RequestParam("avatar") MultipartFile img, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        CommonReturnDTO<Integer> result;

        try {
            result = accountService.signUp(username, password, img, sign);
        } catch (Exception e) {
            e.printStackTrace();
            result = new CommonReturnDTO<>(1, "服务器错误", null);
        }
        response.getWriter().write(JSON.toJSONString(result));
    }
}
