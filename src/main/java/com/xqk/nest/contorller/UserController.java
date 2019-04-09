package com.xqk.nest.contorller;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dao.UserDAO;
import com.xqk.nest.model.*;
import com.xqk.nest.websocket.handlers.SignChannelHandler;
import com.xqk.nest.websocket.util.MessageUtil;
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
    private static long ID = 0;
    private UserDAO userDAO = new UserDAO();
    private MessageUtil messageUtil = new MessageUtil();

    @RequestMapping(value = "/get-info", method = GET)
    public void getUserInfo(@RequestParam("id") long UserId, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        CommonReturnModel<Data> userInfoMsg;
        try {
            userInfoMsg = userDAO.getUserInfo(UserId);
        } catch (Exception e) {
            userInfoMsg = new CommonReturnModel<>(1, "(: 服务器错误");
        }

        response.getWriter().write(JSON.toJSONString(userInfoMsg));
    }

    @RequestMapping(value = "/change-status", method = POST)
    public void changeUserStatus(@RequestParam("id") long id, @RequestParam("status") String status) {
        userDAO.changeUserStatus(id, status);
    }

    @RequestMapping(value = "/change-sign", method = POST)
    public void changeUserSign(@RequestParam("id") long id, @RequestParam("sign") String sign) {
        userDAO.changeUserSign(id, sign);
    }

    @RequestMapping(value = "/find-user", method = GET)
    public void getUserList(@RequestParam("username") String username, javax.servlet.http.HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        username = new String(username.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        Tuple<UserInfo, GroupInfo> result = userDAO.getUserList(username);

        response.getWriter().write(JSON.toJSONString(result));
    }

    //添加用户friendId
    @RequestMapping(value = "/add-friend", method = POST)
    public void addFriend(@RequestParam("id") long id, @RequestParam("friend") long friendId, @RequestParam("group") long groupId, @RequestParam("remark") String remark, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        try {
            UserInfo userInfo = userDAO.getUser(id);
            NotifyUserInfo notifyUserInfo = new NotifyUserInfo(id, userInfo.getAvatar(), userInfo.getUsername(), userInfo.getSign());
            NotifyMsg notifyMsg = new NotifyMsg(ID++, "申请添加你为好友 o_o", friendId, id, groupId, 1, remark, null, 1, "刚刚", notifyUserInfo);
            messageUtil.storeNotifyMsg(SignChannelHandler.CHANNELS, notifyMsg);//将消息存入列表，并提示用户
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}