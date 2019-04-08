package com.xqk.nest.contorller;

import com.xqk.nest.dao.UserDAO;
import com.xqk.nest.model.NotifyMsg;
import com.xqk.nest.model.NotifyUserInfo;
import com.xqk.nest.model.UserInfo;
import com.xqk.nest.websocket.handlers.SignChannelHandler;
import com.xqk.nest.websocket.util.MessageUtil;
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
    private static long ID = 0;
    private UserDAO userDAO = new UserDAO();
    private MessageUtil messageUtil = new MessageUtil();

    @RequestMapping(value = "/get-info", method = GET)
    public void getUserInfo(@RequestParam("id") long UserId, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        String result = userDAO.getUserInfo(UserId);
        response.getWriter().write(result);
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
        username = new String(username.getBytes("ISO-8859-1"), "utf-8");
        response.getWriter().write(userDAO.getUserList(username));
    }

    //添加用户friendId
    @RequestMapping(value = "/add-friend", method = POST)
    public void addFriend(@RequestParam("id") long id,@RequestParam("friend") long friendId, @RequestParam("group") long groupId , @RequestParam("remark") String remark, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        UserInfo userInfo=userDAO.getUser(id);
        System.out.println(userInfo);
        NotifyUserInfo notifyUserInfo = new NotifyUserInfo(id, userInfo.getAvatar(), userInfo.getUsername(), userInfo.getSign());
        NotifyMsg notifyMsg = new NotifyMsg(ID++, "申请添加你为好友 o_o", friendId, id, groupId, 1, remark, null, 1, "刚刚", notifyUserInfo);
        messageUtil.storeNotifyMsg(SignChannelHandler.CHANNELS, notifyMsg);//将消息存入列表，并提示用户
    }
}