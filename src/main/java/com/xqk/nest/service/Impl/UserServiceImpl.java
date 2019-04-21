package com.xqk.nest.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xqk.nest.dao.UserDAO;
import com.xqk.nest.dto.*;
import com.xqk.nest.service.UserService;
import com.xqk.nest.util.MessageUtil;
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

    public CommonReturnDTO<DataDTO> getUserInfo(long userId) {
        return userDAO.getUserInfo(userId);
    }

    /**
     * 修改用户在线状态，并发送消息给好友
     * @param id
     * @param status
     */
    @Override
    public void changeUserStatus(long id, String status) {
        userDAO.changeUserStatus(id,status);
        MessageDTO<StatusMessageDTO> messageDTO=new MessageDTO<>(new StatusMessageDTO(id,status),"changeStatus");
        messageUtil.sendMsg(SignChannelHandler.CHANNELS,messageDTO);
    }

    @Override
    public void changeUserSign(long id, String sign) {
        userDAO.changeUserSign(id,sign);
    }

    @Override
    public Tuple<UserInfoDTO, GroupInfoDTO> getUserList(String username) {
        return userDAO.getUserList(username);
    }

    @Override
    public void addFriend(long userId, long packetId) {
       userDAO.addFriend(userId,packetId);
    }

    /**
     * 添加好友
     * @param id
     * @param friendId
     * @param groupId
     * @param remark
     */
    @Override
    public void sendAddFriendMessage( long id, long friendId, long groupId, String remark) {
        try {
            UserInfoDTO userInfo = userDAO.getUser(id);
            NotifyUserInfoDTO notifyUserInfoDTO = new NotifyUserInfoDTO(id, userInfo.getAvatar(), userInfo.getUsername(), userInfo.getSign());
            NotifyDTO notifyDTO = new NotifyDTO(ID++, "申请添加你为好友 o_o", friendId, id, groupId, 1, remark, null, 1, "刚刚", notifyUserInfoDTO);
            MessageDTO<NotifyDTO> message=new MessageDTO<>(notifyDTO,"notify");
            messageUtil.sendMsg(SignChannelHandler.CHANNELS, message);//将消息存入有序结合中，并提示用户
        }catch(Exception e){
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
