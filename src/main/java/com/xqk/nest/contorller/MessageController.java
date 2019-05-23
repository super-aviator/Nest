package com.xqk.nest.contorller;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dto.*;
import com.xqk.nest.service.MessageService;
import com.xqk.nest.service.UserService;
import com.xqk.nest.websocket.handlers.SignChannelHandler;
import com.xqk.nest.websocket.dto.HistoryChatMessageDTO;
import com.xqk.nest.util.MessageUtil;
import com.xqk.nest.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author  熊乾坤
 */
@Controller
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageUtil messageUtil;

    /**
     * redis的缘故，用户id和群id不能重合但是可以使用分隔符group:的方式解决，所以用户和群的id可以重合
     * 这里有两种查询聊天记录需求，查询群聊的或者查询好友的记录，可以在java中判断url的type字段，然后分情况
     * 也可以在查询时给Mybatis传一个对象，对象包括id和type属性，用Mybatis的条件查询语句进行选择性查找
     * 异常应该在DAO层抛出，controller层捕获。
     */
    @RequestMapping(value = "/get-message", method = GET)
    public void getHistoryMsg(@RequestParam("id") long id, @RequestParam("revid") long revId, @RequestParam("type") String type
            , @RequestParam("current") int start, @RequestParam("limit") int limit, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        CommonReturnDTO<List<HistoryChatMessageDTO>> result = messageService.getPagingMessage(id, revId, type, (start-1)*limit, limit);
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 查询该项的用户历史消息条数
     * @param id 用户id
     * @param revId 接受者id
     * @param type 消息类型
     * @param response 相应对象
     * @throws IOException 写入消息时发生的异常
     */
    @RequestMapping(value = "/get-count", method = GET)
    public void getMessageCount(@RequestParam("id") long id, @RequestParam("revid") long revId, @RequestParam("type") String type
            ,HttpServletResponse response) throws IOException{
        response.setCharacterEncoding("utf-8");
        try{
            response.getWriter().write(String.valueOf(messageService.getMessageCount(id,revId,type)));

        }catch(Exception e){
            response.getWriter().write(0);
        }
    }

    /**
     * 获取id所有的提示消息
     */@SuppressWarnings("unused")
    @RequestMapping(value = "/get-notify", method = POST)
    public void getNotify(@RequestParam("id") long id, @RequestParam("page") long page, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(messageUtil.getNotifyMsg(String.valueOf(id)));
    }

    /**
     * 同意添加好友，向请求发送方发送提示消息
     * * 将id添加到from_group，将uid添加到group
     */
    @RequestMapping(value = "/agree-friend", method = POST)
    public void agreeFriend(@RequestParam("id") long id, @RequestParam("uid") long uid, @RequestParam("fromgroup") long fromGroup
            , @RequestParam("group") long group,
                            HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        AddFriendDTO addFriendDTO = userService.addFriend(id, uid, fromGroup, group);
        response.getWriter().write(JSON.toJSONString(addFriendDTO));
    }

    /**
     * 拒绝添加好友，向uid发送拒绝消息,from字段需要为0，前端会根据此判断是否为已处理消息
     */
    @RequestMapping(value = "/refuse-friend", method = POST)
    public void refuseFriend(@RequestParam("id") long id, @RequestParam("uid") long uid, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        UserInfoDTO userInfo = userService.getUser(id);
        messageUtil.storeNotifyMsg(SignChannelHandler.CHANNELS, new NotifyDTO(0, userInfo.getUsername() + "拒绝了你的请求 （：",
                uid, 0, 0, 1, null, null, 1, "刚刚",
                new NotifyUserInfoDTO(userInfo.getId(), userInfo.getAvatar(), userInfo.getUsername(), userInfo.getSign())));
        response.getWriter().write(JSON.toJSONString(new NotifyReturnDTO(0, 0, null)));
    }

    /**
     * 上传图片接口,待修复
     */
    @RequestMapping(value = "upload-images", method = POST)
    public void uploadImage(@RequestParam("file") MultipartFile image, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        CommonReturnDTO<UploadImageDTO> returnMsg;
        try {
            returnMsg = messageService.uploadImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg = new CommonReturnDTO<>(1, "图片上传失败", null);
        }
        response.getWriter().write(JSON.toJSONString(returnMsg));
    }

    @RequestMapping(value = "upload-file", method = POST)
    public void uploadFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        CommonReturnDTO<UploadFileDTO> returnMsg;
        try {
            returnMsg = messageService.uploadFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg = new CommonReturnDTO<>(1, "文件上传失败", null);
        }
        response.getWriter().write(JSON.toJSONString(returnMsg));
    }

}
