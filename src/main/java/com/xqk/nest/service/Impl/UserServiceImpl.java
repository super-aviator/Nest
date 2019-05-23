package com.xqk.nest.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dao.UserDAO;
import com.xqk.nest.dto.*;
import com.xqk.nest.service.UserService;
import com.xqk.nest.util.MessageUtil;
import com.xqk.nest.util.RedisUtil;
import com.xqk.nest.websocket.dto.MessageDTO;
import com.xqk.nest.websocket.dto.StatusMessageDTO;
import com.xqk.nest.websocket.handlers.SignChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xqk.nest.websocket.dto.MessageDTO;

@Component
public class UserServiceImpl implements UserService {
    private static long ID = 0;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public CommonReturnDTO<DataDTO> getUserInfo(long userId) {
        return userDAO.getUserInfo(userId);
    }

    /**
     * 修改用户在线状态，并发送消息给好友
     *
     * @param id 用户id
     * @param status 用户现在的状态
     */
    @Override
    public void changeUserStatus(long id, String status) {
        userDAO.changeUserStatus(id, status);
        MessageDTO<StatusMessageDTO> messageDTO = new MessageDTO<>(new StatusMessageDTO(id, status), "changeStatus");
        messageUtil.sendMsg(SignChannelHandler.CHANNELS, messageDTO);
    }

    @Override
    public void changeUserSign(long id, String sign) {
        userDAO.changeUserSign(id, sign);
    }

    @Override
    public Tuple<UserInfoDTO, GroupInfoDTO> getUserList(String username) {
        return userDAO.getUserList(username);
    }

    /**
     * 添加好友，对于添加好友消息的接受方，同意请求的消息不需要发送给自己，只需要存储在redis中
     * 对于添加好友消息的发送法方，同意请求的消息需要发送法， 同时需要存储在redis中
     *
     * @param id         接受方id
     * @param uid        添加好友消息发送方id
     * @param from_group uid的分组
     * @param group      id的分组
     */
    @Override
    public AddFriendDTO addFriend(long id, long uid, long from_group, long group) {
        userDAO.addFriend(id, uid, from_group, group);
        redisUtil.addFriend(String.valueOf(uid), String.valueOf(id));

        UserInfoDTO idUserInfo = userDAO.getUser(id);
        messageUtil.storeNotifyMsg(SignChannelHandler.CHANNELS, new NotifyDTO(0, idUserInfo.getUsername() + "同意了你的请求 （：",
                uid, 0, group, 2, null, null, 1, "刚刚",
                new NotifyUserInfoDTO(idUserInfo.getId(), idUserInfo.getAvatar(), idUserInfo.getUsername(), idUserInfo.getSign())));

        UserInfoDTO uidUserInfo = userDAO.getUser(uid);
        redisUtil.pushHistoryNotifyMsg(String.valueOf(id), System.currentTimeMillis(), JSON.toJSONString(new NotifyDTO(0, "你同意了" + uidUserInfo.getUsername() + "的请求 （：",
                id, 0, group, 1, null, null, 1, "刚刚",
                new NotifyUserInfoDTO(uidUserInfo.getId(), uidUserInfo.getAvatar(), uidUserInfo.getUsername(), uidUserInfo.getSign()))));
        return new AddFriendDTO("type", from_group, uidUserInfo);
    }

    /**
     * 添加好友
     *
     * @param id
     * @param friendId
     * @param groupId
     * @param remark
     */
    @Override
    public void sendAddFriendMessage(long id, long friendId, long groupId, String remark) {
        try {
            UserInfoDTO userInfo = userDAO.getUser(id);
            NotifyUserInfoDTO notifyUserInfoDTO = new NotifyUserInfoDTO(id, userInfo.getAvatar(), userInfo.getUsername(), userInfo.getSign());
            NotifyDTO notifyDTO = new NotifyDTO(ID++, "申请添加你为好友 o_o", friendId, id, groupId, 1, remark, null, 1, "刚刚", notifyUserInfoDTO);
            MessageDTO<NotifyDTO> message = new MessageDTO<>(notifyDTO, "notify");
            //将消息存入有序结合中，并提示用户
            messageUtil.sendMsg(SignChannelHandler.CHANNELS, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过id获取用户用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserInfoDTO getUser(long userId) {
        return userDAO.getUser(userId);
    }
}
