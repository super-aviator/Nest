package com.xqk.nest.contorller;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dto.*;
import com.xqk.nest.service.UserService;
import com.xqk.nest.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get-info", method = GET)
    public void getUserInfo(@RequestParam("id") long UserId, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        CommonReturnDTO<DataDTO> userInfoMsg;
        try {
            userInfoMsg = userService.getUserInfo(UserId);
        } catch (Exception e) {
            userInfoMsg = new CommonReturnDTO<>(1, "(: 服务器错误");
        }
        response.getWriter().write(JSON.toJSONString(userInfoMsg));
    }

    @RequestMapping(value = "/change-status", method = POST)
    public void changeUserStatus(@RequestParam("id") long id, @RequestParam("status") String status) {
        userService.changeUserStatus(id, status);
    }

    @RequestMapping(value = "/change-sign", method = POST)
    public void changeUserSign(@RequestParam("id") long id, @RequestParam("sign") String sign) {
        userService.changeUserSign(id, sign);
    }

    @RequestMapping(value = "/find-user", method = GET)
    public void getUserList(@RequestParam("username") String username, javax.servlet.http.HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        username = new String(username.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        Tuple<UserInfoDTO, GroupInfoDTO> result = userService.getUserList(username);
        response.getWriter().write(JSON.toJSONString(result));
    }

    //添加用户friendId
    @RequestMapping(value = "/add-friend", method = POST)
    public void addFriend(@RequestParam("id") long id, @RequestParam("friend") long friendId, @RequestParam("group") long groupId, @RequestParam("remark") String remark, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        userService.sendAddFriendMessage(id,friendId,groupId,remark);
    }
}