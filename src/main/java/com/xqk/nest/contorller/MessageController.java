package com.xqk.nest.contorller;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dao.MessageDAO;
import com.xqk.nest.dao.UserDAO;
import com.xqk.nest.model.*;
import com.xqk.nest.websocket.handlers.SignChannelHandler;
import com.xqk.nest.websocket.model.HistoryChatMessage;
import com.xqk.nest.websocket.util.MessageUtil;
import com.xqk.nest.websocket.util.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/message")
public class MessageController {
    private MessageDAO messageDAO = new MessageDAO();
    private UserDAO userDAO = new UserDAO();
    private MessageUtil messageUtil = new MessageUtil();
    private RedisUtil redisUtil = new RedisUtil();

    /**
     * redis的缘故，用户id和群id不能重合但是可以使用分隔符group:的方式解决，所以用户和群的id可以重合
     * 这里有两种查询聊天记录需求，查询群聊的或者查询好友的记录，可以在java中判断url的type字段，然后分情况
     * 也可以在查询时给Mybatis传一个对象，对象包括id和type属性，用Mybatis的条件查询语句进行选择性查找
     * 异常应该在DAO层抛出，controller层捕获。
     */
    @RequestMapping(value = "/get-message", method = GET)
    public void getHistoryMsg(@RequestParam("id") long id, @RequestParam("revid") long revId, @RequestParam("type") String type, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        CommonReturnModel<List<HistoryChatMessage>> result = messageDAO.getPagingMessage(id, revId, type);
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 获取id所有的提示消息
     */
    @RequestMapping(value = "/get-notify", method = POST)
    public void getNotify(@RequestParam("id") long id, @RequestParam("page") long page, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(messageUtil.getNotifyMsg(String.valueOf(id)));
    }

    /**
     * 同意添加好友，向请求发送方发送提示消息
     * 将id添加到from_group，将uid添加到group
     */
    @RequestMapping(value = "/agree-friend", method = POST)
    public void agreeFriend(@RequestParam("id") long id, @RequestParam("uid") long uid, @RequestParam("fromgroup") long from_group, @RequestParam("group") long group,
                            HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        userDAO.addFriend(id, from_group);
        userDAO.addFriend(uid, group);
        redisUtil.addFriend(String.valueOf(uid), String.valueOf(id));
        UserInfo userInfo = userDAO.getUser(id);
        AddFriendMsg addFriendMsg = new AddFriendMsg("type", from_group, userInfo);
        response.getWriter().write(JSON.toJSONString(addFriendMsg));
    }

    /**
     * 拒绝添加好友，向uid发送拒绝消息
     */
    @RequestMapping(value = "/refuse-friend", method = POST)
    public void refuseFriend(@RequestParam("id") long id, @RequestParam("uid") long uid, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        UserInfo userInfo = userDAO.getUser(id);
        messageUtil.storeNotifyMsg(SignChannelHandler.CHANNELS, new NotifyMsg(0, userInfo.getUsername() + "拒绝了你的请求 （：",
                uid, userInfo.getId(), 0, 0, null, null, 1, "刚刚",
                new NotifyUserInfo(userInfo.getId(), userInfo.getAvatar(), userInfo.getUsername(), userInfo.getSign())));
        response.getWriter().write(JSON.toJSONString(new NotifyMsgResult(0, 0, null)));
    }

    /**
     * 上传图片接口
     * @param image
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "upload-image", method = POST)
    @ResponseBody
    public void uploadImage(@RequestParam("file") MultipartFile image, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        CommonReturnModel<UploadImageMod> returnMsg ;
        try {
            returnMsg = messageDAO.uploadImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg = new CommonReturnModel<>(1, "", null);
        }
        System.out.println(JSON.toJSONString(returnMsg));
        response.getWriter().write(JSON.toJSONString(returnMsg));
    }

}
