package com.xqk.nest.contorller;

import com.xqk.nest.dao.MessageDAO;
import com.xqk.nest.model.NotifyMsg;
import com.xqk.nest.websocket.handlers.SignChannelHandler;
import com.xqk.nest.websocket.util.MessageUtil;
import com.xqk.nest.websocket.util.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/message")
public class MessageController {
    private MessageDAO dao=new MessageDAO();
    private MessageUtil messageUtil=new MessageUtil();

    /**
     * 传入type是为了防止用户和群的id重合，所以用户和群组需要分开建表
     * 因为使用redis做缓存的缘故，用户id和群id不能重合但是可是使用分隔符group:的方式解决
     * 这里有两种查询聊天记录需求，查询群聊的或者查询好友的记录，可以在java中判断url的type字段，然后分情况
     * 也可以在查询时给Mybatis传一个对象，对象包括id和type属性，用Mybatis的条件查询语句进行选择性查找
     */
    @RequestMapping(value = "/get-message",method= GET)
    public void getHistoryMsg(@RequestParam("id") long id, @RequestParam("type") String type, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String result = dao.getPagingMessage(id, type);
        response.getWriter().write(result);
    }

    //获取添加好友消息
    @RequestMapping(value = "/get-notify",method= POST)
    public void getNotify(@RequestParam("id") long id,@RequestParam("page") long page, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(messageUtil.getNotifyMsg(SignChannelHandler.CHANNELS,String.valueOf(id)));
    }

    //同意添加好友
    @RequestMapping(value = "/agree-friend", method = POST)
    public void agreeFriend(@RequestParam("friend") long friendId, @RequestParam("group") long groupId, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        String userId = "";//---------------------------------------------------------------------------------------
    }

    //拒绝添加好友，向uid发送拒绝消息
    @RequestMapping(value = "/refuse-friend", method = POST)
    public void refuseFriend(@RequestParam("uid") long uid,  HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        messageUtil.storeNotifyMsg(SignChannelHandler.CHANNELS,new NotifyMsg());//-------------------
    }
}
