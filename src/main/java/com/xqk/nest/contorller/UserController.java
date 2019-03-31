package com.xqk.nest.contorller;

import com.xqk.nest.dao.UserDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Controller
@RequestMapping(value = "/user")
public class UserController {

    private UserDAO userDAOImp = new UserDAO();

    @RequestMapping(value = "/get-info", method = GET)
    public void getUserInfo(@RequestParam("id") long UserId, HttpServletResponse response) throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        String result = userDAOImp.getUserInfo(UserId);
        response.getWriter().write(result);
    }

    @RequestMapping(value = "/change-status", method = POST)
    public void changeUserStatus(@RequestParam("id") long id, @RequestParam("status") String status) {
        userDAOImp.changeUserStatus(id, status);
    }

    @RequestMapping(value = "/change-sign", method = POST)
    public void changeUserSign(@RequestParam("id") long id, @RequestParam("sign") String sign) {
        userDAOImp.changeUserSign(id, sign);
    }
}